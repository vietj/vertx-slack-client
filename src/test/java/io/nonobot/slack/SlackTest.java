package io.nonobot.slack;

import io.nonobot.core.NonoBot;
import io.nonobot.core.message.MessageRouter;
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

  @Test
  public void testConnectOk(TestContext context) {
    Async done = context.async();
    startServer(context, ws -> {
      ws.close();
      done.countDown();
    });
  }

  @Test
  public void testMessage1(TestContext context) {
    testMessage(context, "<@12345> ping");
  }

  @Test
  public void testMessage2(TestContext context) {
    testMessage(context, "<@12345>:ping");
  }

  @Test
  public void testMessage3(TestContext context) {
    testMessage(context, "<@12345>: ping");
  }

  @Test
  public void testMessage4(TestContext context) {
    testMessage(context, "slack_name ping");
  }

  @Test
  public void testMessage5(TestContext context) {
    testMessage(context, "@slack_name ping");
  }

  @Test
  public void testMessage6(TestContext context) {
    testMessage(context, "@slack_name:ping");
  }

  @Test
  public void testMessage7(TestContext context) {
    testMessage(context, "slack_name:ping");
  }

  private void testMessage(TestContext context, String text) {
    Async done = context.async();
    MessageRouter router = MessageRouter.create(vertx);
    router.handler().respond(".*", msg -> {
      context.assertEquals("ping", msg.body());
      msg.reply("pong");
    }).create();
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

  private void startServer(TestContext context, Handler<ServerWebSocket> handler) {
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(
        bot,
        new SlackOptions().setClientOptions(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(8080)));
    Async started = context.async();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(handler);
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertSuccess(v2 -> {
        started.countDown();
      }));
    }));
    started.awaitSuccess(2000);
  }

  @Test
  public void testConnectHttpError1(TestContext context) {
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    slackAdapter.connect(context.asyncAssertFailure());
  }

  @Test
  public void testConnectHttpError2(TestContext context) {
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    NetServer server = vertx.createNetServer();
    server.connectHandler(socket -> {
      socket.close();
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertFailure());
    }));
  }

  @Test
  public void testConnectWebSocketError(TestContext context) {
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.copy().put("url", "ws://localhost:8081/").encode());
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertFailure());
    }));
  }

  @Test
  public void testServerClose(TestContext context) {
    Async done = context.async(1);
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    slackAdapter.closeHandler(v1 -> done.complete());
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertSuccess(v2 -> {
        slackAdapter.close();
      }));
    }));
  }

  @Test
  public void testClientClose(TestContext context) {
    Async done = context.async(1);
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(WebSocketBase::close);
    slackAdapter.closeHandler(v -> done.complete());
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertSuccess());
    }));
  }
}
