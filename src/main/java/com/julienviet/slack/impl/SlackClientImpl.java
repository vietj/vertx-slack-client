package com.julienviet.slack.impl;

import com.julienviet.slack.ChannelObject;
import com.julienviet.slack.IMObject;
import com.julienviet.slack.SlackChannel;
import com.julienviet.slack.SlackClient;
import com.julienviet.slack.SlackError;
import com.julienviet.slack.SlackMessage;
import com.julienviet.slack.SlackOptions;
import com.julienviet.slack.SlackUser;
import io.vertx.core.AsyncResult;
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
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackClientImpl implements SlackClient {

  private final Vertx vertx;
  private final HttpClient httpClient;
  private final WebClient webClient;
  private final SlackOptions options;
  private WebSocket websocket;
  private long serial;
  private Map<String, ChannelObject> channels = new HashMap<>(); // All channels we belong to id->obj
  private Map<String, IMObject> ims = new HashMap<>(); // All ims we belong to id->obj
  private Map<String, SlackUser> users = new HashMap<>(); // All users id->obj
  private String slackId; // Our own id
  private String slackName; // The slack name
  private Handler<SlackMessage> messageHandler;
  private Handler<Void> closeHandler;
  private Map<String, Future<Void>> pendingAcks = new HashMap<>();

  public SlackClientImpl(Vertx vertx, SlackOptions options) {
    this.options = new SlackOptions(options);
    this.httpClient = vertx.createHttpClient(options);
    this.webClient = WebClient.wrap(httpClient);
    this.vertx = vertx;
  }

  public synchronized SlackClientImpl messageHandler(Handler<SlackMessage> handler) {
    messageHandler = handler;
    return this;
  }

  @Override
  public synchronized SlackClientImpl closeHandler(Handler<Void> handler) {
    closeHandler = handler;
    return this;
  }

  @Override
  public Set<String> imIds() {
    return ims.keySet();
  }

  @Override
  public IMObject im(String id) {
    return ims.get(id);
  }

  @Override
  public Set<String> userIds() {
    return users.keySet();
  }

  @Override
  public SlackUser user(String id) {
    return users.get(id);
  }

  @Override
  public SlackUser userByName(String name) {
    return users.values().stream().filter(channel -> channel.getName().equals(name)).findFirst().orElse(null);
  }

  @Override
  public Set<String> channelIds() {
    return channels.keySet();
  }

  @Override
  public ChannelObject channel(String id) {
    return channels.get(id);
  }

  @Override
  public ChannelObject channelByName(String name) {
    return channels.values().stream().filter(channel -> channel.getName().equals(name)).findFirst().orElse(null);
  }

  private SlackChannel internalChannel(String id) {
    if (id.startsWith("C")) {
      return channel(id);
    } else if (id.startsWith("D")) {
      return im(id);
    } else {
      return null;
    }
  }

  public void start(Handler<AsyncResult<Void>> handler) {
    if (websocket != null) {
      throw new IllegalStateException("Already connected");
    }

    Future<Void> request = Future.future();
    request.setHandler(handler);

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
            if (options.isSsl()) {
              port = 443;
            } else {
              port = 80;
            }
          }

          // Collect all the channels
          JsonArray channelsObj = respObj.getJsonArray("channels");
          for (int i = 0;i < channelsObj.size();i++) {
            JsonObject channelObj = channelsObj.getJsonObject(i);
            addChannel(channelObj);
          }

          // Collect all known users
          JsonArray usersObj = respObj.getJsonArray("users");
          for (int i = 0;i < usersObj.size();i++) {
            JsonObject userObj = usersObj.getJsonObject(i);
            addUser(userObj);
          }

          // Collect all known ims
          JsonArray imsObj = respObj.getJsonArray("ims");
          for (int i = 0;i < imsObj.size();i++) {
            JsonObject userObj = imsObj.getJsonObject(i);
            addIM(userObj);
          }

          slackId = respObj.getJsonObject("self").getString("id");
          slackName = respObj.getJsonObject("self").getString("name");
          httpClient.websocket(port, wsURL.getHost(), wsURL.getPath(), ws -> wsOpen(request, ws),
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
      synchronized (SlackClientImpl.this) {
        if (websocket != null) {
          if (expectedSerial == serial) {
            websocket.writeFinalTextFrame(new JsonObject().put("type", "ping").put("id", UUID.randomUUID().toString()).encode());
          }
          schedulePing(vertx, ++serial);
        }
      }
    });
  }

  private void wsOpen(Future<Void> completion, WebSocket ws) {
    synchronized (this) {
      websocket = ws;
    }
    LinkedList<WebSocketFrame> pendingFrames = new LinkedList<>();
    AtomicReference<Handler<WebSocketFrame>> handler = new AtomicReference<>();
    schedulePing(vertx, serial);
    handler.set(frame -> {
      wsHandle(frame);
    });
    for (WebSocketFrame frame : pendingFrames) {
      wsHandle(frame);
    }
    ws.frameHandler(frame -> {
      if (handler.get() != null) {
        handler.get().handle(frame);
      } else {
        pendingFrames.add(frame);
      }
    });
    ws.closeHandler(v -> {
      Handler<Void> h;
      synchronized (this) {
        websocket = null;
        h = closeHandler;
      }
      if (h != null) {
        h.handle(null);
      }
    });
    if (!completion.isComplete()) {
      completion.complete();
    }
  }

  private synchronized void addChannel(JsonObject channelJson) {
    ChannelObject channel = new ChannelObject(channelJson);
    channels.put(channel.getId(), channel);
  }

  private synchronized void removeChannel(String id) {
    channels.remove(id);
  }

  private synchronized void addUser(JsonObject obj) {
    SlackUser user = new SlackUser(obj);
    users.put(user.getId(), user);
  }

  private synchronized IMObject addIM(JsonObject obj) {
    IMObject im = new IMObject(obj);
    ims.put(im.getId(), im);
    return im;
  }

  private synchronized void removeIM(String id) {
    ims.remove(id);
  }

  @Override
  public SlackClient send(SlackChannel ch, String text) {
    return send(ch, text, null);
  }

  @Override
  public SlackClient send(SlackChannel ch, String text, Handler<AsyncResult<Void>> handler) {
    send(ch.getId(), text, handler);
    return this;
  }

  void send(String channel, String msg, Handler<AsyncResult<Void>> handler) {
    synchronized (SlackClientImpl.this) {
      String id = UUID.randomUUID().toString();
      if (handler != null) {
        pendingAcks.put(id, Future.<Void>future().setHandler(handler));
      }
      serial++; // Need to sync on SlackAdapterImpl
      websocket.writeFinalTextFrame(new JsonObject().
          put("id", id).
          put("type", "message").
          put("channel", channel).
          put("text", msg).encode());
    }
  }

  private synchronized void handleMessage(JsonObject messageObj) {
    String text = messageObj.getString("text");
    String channel =  messageObj.getString("channel");
    String user = messageObj.getString("user");
    if (messageHandler != null) {
      messageHandler.handle(new SlackMessage() {
        @Override
        public SlackUser from() {
          return users.get(user);
        }
        @Override
        public String text() {
          return text;
        }
        @Override
        public SlackChannel channel() {
          return SlackClientImpl.this.internalChannel(channel);
        }
      });
    }
  }

  public SlackClient getOrCreateIM(SlackUser user, Handler<AsyncResult<IMObject>> handler) {
    synchronized (this) {
      String id = user.getId();
      Optional<IMObject> opt = ims.values().stream().filter(im -> im.getUserId().equals(id)).findFirst();
      if (opt.isPresent()) {
        vertx.getOrCreateContext().runOnContext(v -> {
          handler.handle(Future.succeededFuture(opt.get()));
        });
      } else {
        HttpRequest<Buffer> req = webClient.post("/api/im.open");
        req.setQueryParam("token", options.getToken());
        req.setQueryParam("user", id);
        req.setQueryParam("return_im", "true");
        req.send(ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> resp = ar.result();
            if (resp.statusCode() == 200) {
              JsonObject json = resp.bodyAsJsonObject();
              handler.handle(Future.succeededFuture(addIM(json.getJsonObject("channel"))));
            } else {
              handler.handle(Future.failedFuture("Unexpected status: " + resp.statusCode()));
            }
          } else {
            handler.handle(Future.failedFuture(ar.cause()));
          }
        });
      }
    }
    return this;
  }

  @Override
  public SlackClient joinChannel(ChannelObject channel, Handler<AsyncResult<Void>> handler) {
    synchronized (this) {
      if (channel.isMember()) {
        vertx.getOrCreateContext().runOnContext(v -> {
          handler.handle(Future.succeededFuture());
        });
      } else {
        HttpRequest<Buffer> req = webClient.post("/api/channels.join");
        req.setQueryParam("token", options.getToken());
        req.setQueryParam("name", channel.getName());
        req.send(ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> resp = ar.result();
            if (resp.statusCode() == 200) {
              JsonObject json = resp.bodyAsJsonObject();
              Boolean ok = json.getBoolean("ok");
              if (Boolean.TRUE.equals(ok)) {
                addChannel(json.getJsonObject("channel"));
                handler.handle(Future.succeededFuture());
              } else {
                JsonObject error = json.getJsonObject("error");
                SlackError err = new SlackError(error);
                handler.handle(Future.failedFuture(err));
              }
            } else {
              handler.handle(Future.failedFuture("Unexpected status: " + resp.statusCode()));
            }
          } else {
            handler.handle(Future.failedFuture(ar.cause()));
          }
        });
      }
    }
    return this;
  }

  void wsHandle(WebSocketFrame frame) {
    JsonObject json = new JsonObject(frame.textData());
    String type = json.getString("type");
    if (type != null) {
      switch (type) {
        case "channel_left":
          removeChannel(json.getString("channel"));
          return;
        case "channel_joined":
          addChannel(json.getJsonObject("channel"));
          return;
        case "team_join":
          addUser(json.getJsonObject("user"));
          return;
        case "im_created":
          addIM(json.getJsonObject("channel"));
          return;
        case "im_close":
          removeIM(json.getString("channel"));
          return;
        case "message":
          handleMessage(json);
          return;
        case "presence_change":
        case "pong": {
          return;
        }
      }
    }
    Boolean ok = json.getBoolean("ok");
    if (ok != null) {
      String replyTo = json.getString("reply_to");
      if (replyTo != null) {
        Future<Void> ack;
        synchronized (SlackClientImpl.this) {
           ack = pendingAcks.remove(replyTo);
        }
        if (ack != null) {
          if (ok) {
            ack.handle(Future.succeededFuture());
          } else {
            JsonObject error = json.getJsonObject("error");
            SlackError err = new SlackError(error);
            ack.handle(Future.failedFuture(err));
          }
        }
        return;
      }
    }
    System.out.println("Unhandled frame " + json);
  }

  @Override
  public synchronized void close() {
    if (websocket != null) {
      websocket.close();
    }
  }
}
