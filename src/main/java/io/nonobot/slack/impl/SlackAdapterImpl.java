package io.nonobot.slack.impl;

import io.nonobot.core.adapter.ConnectionRequest;
import io.nonobot.core.client.BotClient;
import io.nonobot.core.client.ReceiveOptions;
import io.nonobot.slack.SlackAdapter;
import io.nonobot.slack.SlackOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackAdapterImpl implements SlackAdapter {

  private final SlackOptions options;
  private WebSocket websocket;
  private long serial;
  private Map<String, String> channels = new HashMap<>(); // All channels we belong to id->name
  private Map<String, String> users = new HashMap<>(); // All users id->name
  private String slackId; // Our own id
  private String slackName; // The slack name

  public SlackAdapterImpl(SlackOptions options) {
    this.options = new SlackOptions(options);
  }

  @Override
  public void handle(ConnectionRequest request) {
    if (websocket != null) {
      throw new IllegalStateException("Already connected");
    }

    BotClient client = request.client();

    client.closeHandler(v -> {
      if (websocket != null) {
        websocket.close();
      }
    });

    Vertx vertx = client.vertx();

    HttpClient httpClient = vertx.createHttpClient(options.getClientOptions());
    String token = this.options.getToken();

    HttpClientRequest req = httpClient.get("/api/rtm.start?token=" + token, resp -> {
      if (resp.statusCode() == 200) {
        Buffer buffer = Buffer.buffer();
        resp.handler(buffer::appendBuffer);
        resp.exceptionHandler(err -> {
          if (!request.isComplete()) {
            request.fail(err);
          }
        });
        resp.endHandler(v1 -> {
          JsonObject respObj = buffer.toJsonObject();
          URL wsURL;
          try {
            wsURL = new URL(respObj.getString("url").replace("ws", "http"));
          } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
          }
          int port = wsURL.getPort();
          if (port == -1) {
            if (options.getClientOptions().isSsl()) {
              port = 443;
            } else {
              port = 80;
            }
          }

          // Collect all the channels we belong to
          JsonArray channelsObj = respObj.getJsonArray("channels");
          for (int i = 0;i < channelsObj.size();i++) {
            JsonObject channelObj = channelsObj.getJsonObject(i);
            if (channelObj.getBoolean("is_member")) {
              joinChannel(channelObj.getString("id"), channelObj.getString("name"));
            }
          }

          // Collect all known users
          JsonArray usersObj = respObj.getJsonArray("users");
          for (int i = 0;i < usersObj.size();i++) {
            JsonObject userObj = usersObj.getJsonObject(i);
            addUser(userObj.getString("id"), userObj.getString("name"));
          }

          slackId = respObj.getJsonObject("self").getString("id");
          slackName = respObj.getJsonObject("self").getString("name");
          httpClient.websocket(port, wsURL.getHost(), wsURL.getPath(), ws -> wsOpen(client, request, ws),
              err -> {
                if (!request.isComplete()) {
                  request.fail(err);
                }
              });
        });
      } else {
        System.out.println("Got response " + resp.statusCode() + " / " + resp.statusMessage());
      }
    });
    req.exceptionHandler(err -> {
      if (!request.isComplete()) {
        request.fail(err);
      }
    });
    req.end();
  }

  private synchronized void schedulePing(Vertx vertx, long expectedSerial) {
    vertx.setTimer(4000, timerID -> {
      synchronized (SlackAdapterImpl.this) {
        if (websocket != null) {
          if (expectedSerial == serial) {
            websocket.writeFinalTextFrame(new JsonObject().put("type", "ping").put("id", UUID.randomUUID().toString()).encode());
          }
          schedulePing(vertx, ++serial);
        }
      }
    });
  }

  private void wsOpen(BotClient client, Future<Void> completion, WebSocket ws) {
    synchronized (this) {
      websocket = ws;
    }
    LinkedList<WebSocketFrame> pendingFrames = new LinkedList<>();
    AtomicReference<Handler<WebSocketFrame>> handler = new AtomicReference<>();
    client.alias(Arrays.asList("<@" + slackId + ">", slackName, "@" + slackName));
    schedulePing(client.vertx(), serial);
    handler.set(frame -> {
      wsHandle(frame, client);
    });
    for (WebSocketFrame frame : pendingFrames) {
      wsHandle(frame, client);
    }
    ws.frameHandler(frame -> {
      if (handler.get() != null) {
        handler.get().handle(frame);
      } else {
        pendingFrames.add(frame);
      }
    });
    client.messageHandler(msg -> {
      String chatId = msg.chatId();
      if (chatId.startsWith("@")) {
        String userName = chatId.substring(1);
        for (Map.Entry<String, String> entry : users.entrySet()) {
          if (entry.getValue().equals(userName)) {
            sendToChannel(entry.getKey(), msg.body());
            return;
          }
        }
      } else if (chatId.startsWith("#")) {
        String channelName = chatId.substring(1);
        for (Map.Entry<String, String> entry : channels.entrySet()) {
          if (entry.getValue().equals(channelName)) {
            sendToChannel(entry.getKey(), msg.body());
            return;
          }
        }
      } else {
        sendToChannel(chatId, msg.body());
      }
    });
    ws.closeHandler(v -> {
      wsClose(client);
    });
    if (!completion.isComplete()) {
      completion.complete();
    }
  }

  private synchronized void leaveChannel(String channelId) {
    String name = channels.remove(channelId);
    System.out.println("Left id=" + channelId + " name=" + name);
  }

  private synchronized void joinChannel(String channelId, String channelName) {
    System.out.println("Joined id=" + channelId + " name=" + channelName);
    channels.put(channelId, channelName);
  }

  private synchronized void addUser(String userId, String userName) {
    System.out.println("User id=" + userId + " name=" + userName);
    users.put(userId, userName);
  }

  private void sendToChannel(String channel, String msg) {
    synchronized (SlackAdapterImpl.this) {
      serial++; // Need to sync on SlackAdapterImpl
      websocket.writeFinalTextFrame(new JsonObject().
          put("id", UUID.randomUUID().toString()).
          put("type", "message").
          put("channel", channel).
          put("text", msg).encode());
    }
  }

  private synchronized void handleMessage(BotClient client, String msg, String channelId) {
    System.out.println("Handling message from " + channelId + ": " + msg);
    String chatId = channels.get(channelId);
    if (chatId != null) {
      chatId = "#" + chatId;
    } else {
      chatId = users.get(channelId);
      if (chatId != null) {
        chatId = "@" + chatId;
      } else {
        chatId = channelId;
      }
    }
    client.receiveMessage(new ReceiveOptions().setChatId(chatId), msg, reply -> {
      if (reply.succeeded()) {
        sendToChannel(channelId, reply.result());
      } else {
        System.out.println("no reply for " + msg);
        reply.cause().printStackTrace();
      }
    });
  }

  void wsHandle(WebSocketFrame frame, BotClient client) {
    JsonObject json = new JsonObject(frame.textData());
    String type = json.getString("type", "");
    switch (type) {
      case "channel_left": {
        String id = json.getString("channel");
        System.out.println("Left " + id);
        leaveChannel(id);
        break;
      }
      case "channel_joined": {
        String id = json.getJsonObject("channel").getString("id");
        String name = json.getJsonObject("channel").getString("name");
        joinChannel(id, name);
        break;
      }
      case "team_join": {
        String id = json.getJsonObject("user").getString("id");
        String name = json.getJsonObject("user").getString("name");
        addUser(id, name);
        users.put(id, name);
        break;
      }
      case "message":
        System.out.println("json = " + json);
        String text = json.getString("text");
        String channel = json.getString("channel");
        if (text != null) {
          handleMessage(client, text, channel);
        } else {
          // What case ?
        }
        break;
      case "presence_change":
      case "pong": {
        break;
      }
      default: {
        System.out.println("Unhandled message " + json);
        break;
      }
    }
  }

  private void wsClose(BotClient client) {
    synchronized (this) {
      websocket = null;
    }
    client.close();
  }
}
