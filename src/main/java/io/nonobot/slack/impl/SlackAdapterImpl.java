package io.nonobot.slack.impl;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackAdapterImpl implements SlackAdapter {

  private final SlackOptions options;
  private WebSocket websocket;
  private long serial;
  private Set<String> channels = new HashSet<>(); // All channels we belong to
  private String slackId; // Our own id
  private String slackName; // The slack name

  public SlackAdapterImpl(SlackOptions options) {
    this.options = new SlackOptions(options);
  }

  @Override
  public void connect(BotClient client, Future<Void> completion) {
    if (websocket != null) {
      throw new IllegalStateException("Already connected");
    }

    Vertx vertx = client.bot().vertx();

    HttpClient httpClient = vertx.createHttpClient(options.getClientOptions());
    String token = this.options.getToken();

    HttpClientRequest req = httpClient.get("/api/rtm.start?token=" + token, resp -> {
      if (resp.statusCode() == 200) {
        Buffer buffer = Buffer.buffer();
        resp.handler(buffer::appendBuffer);
        resp.exceptionHandler(err -> {
          if (!completion.isComplete()) {
            completion.fail(err);
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
              joinChannel(channelObj.getString("id"));
            }
          }

          slackId = respObj.getJsonObject("self").getString("id");
          slackName = respObj.getJsonObject("self").getString("name");
          httpClient.websocket(port, wsURL.getHost(), wsURL.getPath(), ws -> wsOpen(client, completion, ws),
              err -> {
                if (!completion.isComplete()) {
                  completion.fail(err);
                }
              });
        });
      } else {
        System.out.println("Got response " + resp.statusCode() + " / " + resp.statusMessage());
      }
    });
    req.exceptionHandler(err -> {
      if (completion != null && !completion.isComplete()) {
        completion.fail(err);
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
    client.rename(Arrays.asList("<@" + slackId + ">", slackName, "@" + slackName));
    schedulePing(client.bot().vertx(), serial);
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
    ws.closeHandler(v -> {
      wsClose(client);
    });
    if (!completion.isComplete()) {
      completion.complete();
    }
  }

  private synchronized void leaveChannel(String channel) {
    channels.remove(channel);
  }

  private synchronized void joinChannel(String channel) {
    channels.add(channel);
  }

  private synchronized void handleMessage(BotClient client, String msg, String channel) {
    System.out.println("Handling message from " + channel + ": " + msg);
    client.receiveMessage(new ReceiveOptions(), msg, reply -> {
      if (reply.succeeded()) {
        synchronized (SlackAdapterImpl.this) {
          serial++; // Need to sync on SlackAdapterImpl
          websocket.writeFinalTextFrame(new JsonObject().
              put("id", UUID.randomUUID().toString()).
              put("type", "message").
              put("channel", channel).
              put("text", reply.result()).encode());
        }
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
        String channel = json.getString("channel");
        System.out.println("Left " + channel);
        leaveChannel(channel);
        break;
      }
      case "channel_joined": {
        String channel = json.getJsonObject("channel").getString("id");
        System.out.println("Joined " + channel);
        joinChannel(channel);
        break;
      }
      case "message":
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

  @Override
  public synchronized void close() {
    if (websocket != null) {
      websocket.close();
    }
  }
}
