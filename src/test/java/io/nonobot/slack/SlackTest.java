package io.nonobot.slack;

import io.nonobot.core.NonoBot;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
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
      put("self", new JsonObject().put("id", "")).
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
    Async done = context.async(2);
    NonoBot bot = NonoBot.create(vertx);
    SlackAdapter slackAdapter = SlackAdapter.create(bot, new SlackOptions().setClientOptions(new HttpClientOptions().
        setDefaultHost("localhost").setDefaultPort(8080)));
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("Content-Type", "application/json").end(json.encode());
    });
    server.websocketHandler(ws -> {
      ws.close();
      done.countDown();
    });
    server.listen(8080, "localhost", context.asyncAssertSuccess(v -> {
      slackAdapter.connect(context.asyncAssertSuccess(v2 -> {
        done.countDown();
      }));
    }));
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
