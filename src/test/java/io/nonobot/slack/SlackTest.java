package io.nonobot.slack;

import io.nonobot.core.Bot;
import io.nonobot.core.client.BotClient;
import io.nonobot.core.client.ClientOptions;
import io.nonobot.core.client.impl.BotClientImpl;
import io.nonobot.core.handler.MessageRouter;
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
      put("channels", new JsonArray()).
      put("self", new JsonObject().put("id", "12345").put("name", "slack_name")).
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

  private BotClient client(Bot bot) throws Exception {
    return client(bot, null);
  }

  private BotClient client(Bot bot, Handler<Void> closeHandler) throws Exception {
    return new BotClientImpl(bot, vertx.getOrCreateContext(), new ClientOptions()) {
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
  public void testMessage1(TestContext context) throws Exception {
    testMessage(context, "<@12345> ping");
  }

  @Test
  public void testMessage2(TestContext context) throws Exception {
    testMessage(context, "<@12345>:ping");
  }

  @Test
  public void testMessage3(TestContext context) throws Exception {
    testMessage(context, "<@12345>: ping");
  }

  @Test
  public void testMessage4(TestContext context) throws Exception {
    testMessage(context, "slack_name ping");
  }

  @Test
  public void testMessage5(TestContext context) throws Exception {
    testMessage(context, "@slack_name ping");
  }

  @Test
  public void testMessage6(TestContext context) throws Exception {
    testMessage(context, "@slack_name:ping");
  }

  @Test
  public void testMessage7(TestContext context) throws Exception {
    testMessage(context, "slack_name:ping");
  }

  private void testMessage(TestContext context, String text) throws Exception {
    Async done = context.async();
    MessageRouter router = MessageRouter.getShared(vertx);
    router.respond(".*", msg -> {
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
      ws.writeFinalTextFrame(new JsonObject().put("type", "message").put("text", text).encode());
    });
  }

  private void startServer(TestContext context, Handler<ServerWebSocket> handler) throws Exception {
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot);
    SlackAdapter slackAdapter = SlackAdapter.create(
        new SlackOptions().setClientOptions(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(8080)));
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
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot);
    SlackAdapter slackAdapter = SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    Future<Void> completionFuture = Future.future();
    completionFuture.setHandler(context.asyncAssertFailure());
    slackAdapter.connect(client, completionFuture);
  }

  @Test
  public void testConnectHttpError2(TestContext context) throws Exception {
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot);
    SlackAdapter slackAdapter = SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    NetServer server = vertx.createNetServer();
    server.connectHandler(socket -> {
      socket.close();
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      Future<Void> completionFuture = Future.future();
      completionFuture.setHandler(context.asyncAssertFailure());
      slackAdapter.connect(client, completionFuture);
    }));
  }

  @Test
  public void testConnectWebSocketError(TestContext context) throws Exception {
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot);
    SlackAdapter slackAdapter = SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
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
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot, v -> done.complete());
    SlackAdapter slackAdapter = SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
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
    Bot bot = Bot.create(vertx);
    BotClient client = client(bot, v -> done.complete());
    SlackAdapter slackAdapter = SlackAdapter.create(new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
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
