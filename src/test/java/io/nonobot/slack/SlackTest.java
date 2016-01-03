package io.nonobot.slack;

import io.nonobot.core.Bot;
import io.nonobot.core.adapter.BotAdapter;
import io.nonobot.core.chat.ChatRouter;
import io.nonobot.core.client.BotClient;
import io.nonobot.core.client.ClientOptions;
import io.nonobot.core.client.impl.BotClientImpl;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
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

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class SlackTest {

  private static JsonObject json = new JsonObject().
      put("channels", new JsonArray().add(new JsonObject().put("id", "54321").put("name", "the_room").put("is_member", true))).
      put("users", new JsonArray().add(new JsonObject().put("id", "12345").put("name", "the_user"))).
      put("self", new JsonObject().put("id", "12345").put("name", "the_user")).
      put("url", "ws://localhost:8080/");

  protected Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

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

  @Test
  public void testConnectOk(TestContext context) throws Exception {
    Async done = context.async();
    startServer(context, ws -> {
      ws.close();
      done.countDown();
    });
  }

  @Test
  public void testReceiveRoomMessage1(TestContext context) throws Exception {
    testReceiveMessage(context, "<@12345> ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage2(TestContext context) throws Exception {
    testReceiveMessage(context, "<@12345>:ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage3(TestContext context) throws Exception {
    testReceiveMessage(context, "<@12345>: ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage4(TestContext context) throws Exception {
    testReceiveMessage(context, "the_user ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage5(TestContext context) throws Exception {
    testReceiveMessage(context, "@the_user ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage6(TestContext context) throws Exception {
    testReceiveMessage(context, "@the_user:ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveRoomMessage7(TestContext context) throws Exception {
    testReceiveMessage(context, "the_user:ping", "54321", "#the_room");
  }

  @Test
  public void testReceiveDirectMessage(TestContext context) throws Exception {
    testReceiveMessage(context, "the_user:ping", "12345", "@the_user");
  }

  private void testReceiveMessage(TestContext context, String text, String channelId, String expectedChatId) throws Exception {
    Async done = context.async();
    ChatRouter router = Bot.getShared(vertx).chatRouter();
    router.respond(".*", msg -> {
      context.assertEquals(expectedChatId, msg.chatId());
      context.assertEquals("ping", msg.body());
      msg.reply("pong");
    });
    startServer(context, ws -> {
      ws.handler(buf -> {
        JsonObject msg = buf.toJsonObject();
        context.assertEquals("message", msg.getString("type"));
        context.assertEquals("pong", msg.getString("text"));
        ws.close();
        done.complete();
      });
      ws.writeFinalTextFrame(new JsonObject().put("type", "message").put("text", text).put("channel", channelId).encode());
    });
  }

  @Test
  public void testSendMessageToRoomName(TestContext context) throws Exception {
    testSendMessage(context, "#the_room", "54321");
  }

  @Test
  public void testSendMessageToRoomId(TestContext context) throws Exception {
    testSendMessage(context, "54321", "54321");
  }

  @Test
  public void testSendMessageToUserName(TestContext context) throws Exception {
    testSendMessage(context, "@the_user", "12345");
  }

  @Test
  public void testSendMessageToUserId(TestContext context) throws Exception {
    testSendMessage(context, "12345", "12345");
  }

  private void testSendMessage(TestContext context, String chatId, String expectedChannel) throws Exception {
    Async async = context.async();
    startServer(context, ws -> {
      ws.handler(buff -> {
        JsonObject json = buff.toJsonObject();
        context.assertEquals("message", json.getString("type"));
        context.assertEquals(expectedChannel, json.getString("channel"));
        context.assertEquals("the_body", json.getString("text"));
        async.complete();
      });
    });
    vertx.eventBus().send("bots.nono.outbound", new JsonObject().put("chatId", chatId).put("body", "the_body"));
  }

  private void startServer(TestContext context, Handler<ServerWebSocket> handler) throws Exception {
    BotClient client = client();
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(
        new SlackOptions().setClientOptions(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(8080))));
    Async started = context.async();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(handler);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertSuccess(v2 -> {
        started.countDown();
      }));
      slackAdapter.connect(client, completionFuture);
    }));
    started.awaitSuccess(2000);
  }

  @Test
  public void testConnectHttpError1(TestContext context) throws Exception {
    BotClient client = client();
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080))));
    Future<Void> completionFuture = Future.future();
    completionFuture.setHandler(context.asyncAssertFailure());
    slackAdapter.connect(client, completionFuture);
  }

  @Test
  public void testConnectHttpError2(TestContext context) throws Exception {
    BotClient client = client();
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080))));
    NetServer server = vertx.createNetServer();
    server.connectHandler(NetSocket::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertFailure());
      slackAdapter.connect(client, completionFuture);
    }));
  }

  @Test
  public void testConnectWebSocketError(TestContext context) throws Exception {
    BotClient client = client();
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080))));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.copy().put("url", "ws://localhost:8081/").encode());
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertFailure());
      slackAdapter.connect(client, completionFuture);
    }));
  }

  @Test
  public void testServerClose(TestContext context) throws Exception {
    Async done = context.async(1);
    BotClient client = client(v -> done.complete());
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080))));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertSuccess(v2 -> {
        slackAdapter.close();
      }));
      slackAdapter.connect(client, completionFuture);
    }));
  }

  @Test
  public void testClientClose(TestContext context) throws Exception {
    Async done = context.async(1);
    BotClient client = client(v -> done.complete());
    BotAdapter slackAdapter = BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080))));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertSuccess());
      slackAdapter.connect(client, completionFuture);
    }));
  }
}
