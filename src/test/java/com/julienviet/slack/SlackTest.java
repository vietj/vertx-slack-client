package com.julienviet.slack;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketBase;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class SlackTest {

  private static final SlackOptions TEST_OPTIONS = new SlackOptions()
      .setSsl(false)
      .setTrustAll(false)
      .setDefaultHost("localhost")
      .setDefaultPort(8080);

  private JsonObject json;
  private Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    json = new JsonObject().
        put("channels", new JsonArray().add(
            new JsonObject()
                .put("id", "C0001")
                .put("name", "the_room")
                .put("is_member", true)
        ).add(new JsonObject()
            .put("id", "C0002")
            .put("name", "other_room")
            .put("is_member", false))).
        put("users", new JsonArray()
            .add(new JsonObject().put("id", "U0001").put("name", "Bob"))
            .add(new JsonObject().put("id", "U0002").put("name", "Alice"))).
        put("ims", new JsonArray()
            .add(new JsonObject().put("id", "D0001").put("user", "U0002"))
        ).
        put("self", new JsonObject().put("id", "U0001").put("name", "Bob")).
        put("url", "ws://localhost:8080/");
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

/*
  private BotClient client() throws Exception {
    return client(null);
  }

  private BotClient client(Handler<Void> closeHandler) throws Exception {
    return new BotClientImpl(vertx, vertx.getOrCreateContext(), new ClientOptions(), ar -> {}) {
      @Override
      public void close() {
        if (closeHandler != null) {
          closeHandler.handle(null);
        }
      }
    };
  }
*/

  @Test
  public void testConnectOk(TestContext context) throws Exception {
    Async done = context.async();
    startServer(context, ws -> {
      ws.close();
      done.countDown();
    });
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v -> {
      context.assertEquals(new HashSet<>(Arrays.asList("C0001", "C0002")), client.channelIds());
      ChannelObject c0001 = client.channel("C0001");
      context.assertEquals("the_room", c0001.getName());
      context.assertEquals(true, c0001.isMember());
      ChannelObject c0002 = client.channel("C0002");
      context.assertEquals("other_room", c0002.getName());
      context.assertEquals(false, c0002.isMember());
      Set<String> expectedUserIds = new HashSet<>();
      expectedUserIds.add("U0001");
      expectedUserIds.add("U0002");
      context.assertEquals(expectedUserIds, client.userIds());
      SlackUser bob = client.user("U0001");
      SlackUser alice = client.user("U0002");
      context.assertEquals("U0001", bob.getId());
      context.assertEquals("Bob", bob.getName());
      context.assertEquals("U0002", alice.getId());
      context.assertEquals("Alice", alice.getName());
    }));
  }

  @Test
  public void testReceiveMessageFromChannel(TestContext context) throws Exception {
    testReceiveMessage(context, "U0002", "the_text", "D0001", "Alice");
  }

  private void testReceiveMessage(TestContext context, String sender, String text, String channelId, String expectedUserName) throws Exception {
    Async done = context.async();
    startServer(context, ws -> {
      ws.handler(buf -> {
        JsonObject msg = buf.toJsonObject();
        context.assertEquals("message", msg.getString("type"));
        context.assertEquals("pong", msg.getString("text"));
        ws.close();
        done.complete();
      });
      ws.writeFinalTextFrame(new JsonObject()
          .put("user", sender)
          .put("type", "message")
          .put("text", text)
          .put("channel", channelId)
          .encode());
    });
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.messageHandler(msg -> {
      context.assertEquals(channelId, msg.channel().getId());
      context.assertEquals(expectedUserName, expectedUserName != null ? msg.from().getName() : null);
      context.assertEquals(text, msg.text());
      client.send(msg.channel(), "pong");
    });
    client.start(context.asyncAssertSuccess());

  }

  @Test
  public void testSendToChannel(TestContext context) throws Exception {
    Async async = context.async();
    testSendToChannel(context, "C0001", client -> client.send(client.channel("C0001"), "the_body", context.asyncAssertSuccess(v -> async.complete())));
  }

  @Test
  public void testSendToChannelByName(TestContext context) throws Exception {
    Async async = context.async();
    testSendToChannel(context, "C0001", client -> client.send(client.channelByName("the_room"), "the_body", context.asyncAssertSuccess(v -> async.complete())));
  }

  private void testSendToChannel(TestContext context, String expectedChannel, Handler<SlackClient> handler) throws Exception {
    startServer(context, ws -> {
      ws.handler(buff -> {
        JsonObject json = buff.toJsonObject();
        context.assertEquals("message", json.getString("type"));
        context.assertEquals(expectedChannel, json.getString("channel"));
        context.assertEquals("the_body", json.getString("text"));
        ws.writeFinalTextFrame(new JsonObject()
            .put("ok", true)
            .put("reply_to", json.getString("id"))
            .encode());
      });
    });
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v -> handler.handle(client)));
  }

  @Test
  public void testSendToNonMemberChannel(TestContext context) throws Exception {
    Async async = context.async();
    startServer(context, ws -> {
      ws.handler(buff -> {
        JsonObject json = buff.toJsonObject();
        context.assertEquals("message", json.getString("type"));
        context.assertEquals("C0002", json.getString("channel"));
        context.assertEquals("the_body", json.getString("text"));
        ws.writeFinalTextFrame(new JsonObject()
            .put("ok", false)
            .put("reply_to", json.getString("id"))
            .put("error", new JsonObject()
                .put("code", 0)
                .put("msg", "the_error"))
            .encode());
      });
    });
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v -> client.send(client.channel("C0002"), "the_body", context.asyncAssertFailure(err -> {
      async.complete();
    }))));
  }

  @Test
  public void testJoinChannel(TestContext context) throws Exception {
    Async async = context.async();
    startServer(context);
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v1 -> {
      ChannelObject ch = client.channel("C0002");
      context.assertFalse(ch.isMember());
      client.joinChannel(ch, context.asyncAssertSuccess(v2 -> {
        context.assertTrue(client.channel("C0002").isMember());
        async.complete();
      }));
    }));
  }

  @Test
  public void testCannotJoinChannel(TestContext context) throws Exception {
    Async async = context.async();
    startServer(context);
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v1 -> {
      client.joinChannel(new ChannelObject(new JsonObject().put("id", "C9999").put("is_member", false).put("name", "phantom_channel")), context.asyncAssertFailure(err -> {
        async.complete();
      }));
    }));
  }

  @Test
  public void testSendToExistingIM(TestContext context) throws Exception {
    testSendToIM(context, new AtomicReference<>("D0001"), client -> client.getOrCreateIM(client.user("U0002"), context.asyncAssertSuccess(im -> client.send(im, "the_body"))));
  }

  @Test
  public void testSendToIM(TestContext context) throws Exception {
    AtomicReference<String> ref = new AtomicReference<>();
    testSendToIM(context, ref, client -> client.getOrCreateIM(client.user("U0001"), context.asyncAssertSuccess(im -> {
      ref.set(im.getId());
      client.send(im, "the_body");
    })));
  }

  private void testSendToIM(TestContext context, AtomicReference<String> expectedChannel, Handler<SlackClient> handler) throws Exception {
    Async async = context.async();
    startServer(context, ws -> {
      ws.handler(buff -> {
        JsonObject json = buff.toJsonObject();
        context.assertEquals("message", json.getString("type"));
        context.assertEquals(expectedChannel.get(), json.getString("channel"));
        context.assertEquals("the_body", json.getString("text"));
        async.complete();
      });
    });
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.start(context.asyncAssertSuccess(v -> handler.handle(client)));
  }

  private void startServer(TestContext context) throws Exception {
    startServer(context, ws -> {});
  }

  private void startServer(TestContext context, Handler<ServerWebSocket> handler) throws Exception {
    Async started = context.async();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      switch (req.path()) {
        case "/api/rtm.start":
          req.response().putHeader("Content-Type", "application/json").end(json.encode());
          break;
        case "/api/im.open": {
          String user = req.getParam("user");
          String returnIm = req.getParam("return_im");
          JsonArray ims = json.getJsonArray("ims");
          JsonObject channel = null;
          for (int i = 0;i < ims.size();i++) {
            JsonObject im = ims.getJsonObject(i);
            if (im.getString("user").equals(user)) {
              channel = im;
              break;
            }
          }
          if (channel == null) {
            JsonArray users = json.getJsonArray("users");
            for (int i = 0;i < users.size();i++) {
              JsonObject u = users.getJsonObject(i);
              if (u.getString("id").equals(user)) {
                channel = new JsonObject().put("user", user).put("id", "D" + Math.abs(new Random().nextInt()));
                ims.add(channel);
                break;
              }
            }
            if (channel == null) {
              throw new UnsupportedOperationException("todo");
            }
          }
          JsonObject body = new JsonObject().put("ok", true).put("channel", channel.getString("id"));
          if (returnIm.equals("true")) {
            body.put("channel", channel);
          }
          req.response().end(body.encode());
          break;
        }
        case "/api/channels.join": {
          String name = req.getParam("name");
          JsonArray channels = json.getJsonArray("channels");
          for (int i = 0;i < channels.size();i++) {
            JsonObject channel = channels.getJsonObject(i);
            if (channel.getString("name").equals(name)) {
              JsonObject json = new JsonObject().put("ok", true);
              if (channel.getBoolean("is_member")) {
                json.put("already_in_channel", true);
              } else {
                channel.put("is_member", true);
              }
              json.put("channel", channel);
              req.response().end(json.encode());
              return;
            }
          }
          JsonObject json = new JsonObject().put("ok", false).put("error", new JsonObject().put("code", 0).put("msg", "the_error"));
          req.response().end(json.encode());
          break;
        }
        default:
          System.out.println(req.path());
          req.response().setStatusCode(404).end();
          break;
      }
    });
    server.websocketHandler(handler);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      started.countDown();
    }));
    started.awaitSuccess(2000);
  }

  @Test
  public void testConnectHttpError1(TestContext context) throws Exception {
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    Future<Void> completionFuture = Future.future();
    completionFuture.setHandler(context.asyncAssertFailure());
    client.start(completionFuture.completer());
  }

  @Test
  public void testConnectHttpError2(TestContext context) throws Exception {
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    NetServer server = vertx.createNetServer();
    server.connectHandler(NetSocket::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertFailure());
      client.start(completionFuture.completer());
    }));
  }

  @Test
  public void testConnectWebSocketError(TestContext context) throws Exception {
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.put("url", "ws://localhost:8081/").encode());
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertFailure());
      client.start(completionFuture.completer());
    }));
  }

  @Test
  public void testServerClose(TestContext context) throws Exception {
    Async done = context.async(1);
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.closeHandler(v -> done.complete());
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertSuccess(v2 -> {
        client.close();
      }));
      client.start(completionFuture.completer());
    }));
  }

  @Test
  public void testClientClose(TestContext context) throws Exception {
    Async done = context.async(1);
    SlackClient client = SlackClient.create(vertx, TEST_OPTIONS);
    client.closeHandler(v -> done.complete());
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertSuccess());
      client.start(completionFuture.completer());
    }));
  }
}
