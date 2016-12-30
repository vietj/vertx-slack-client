package com.julienviet.slack;

import com.julienviet.slack.impl.SlackClientImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.Set;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SlackClient {

  static SlackClient create(Vertx vertx, SlackOptions options) {
    return new SlackClientImpl(vertx, options);
  }

  @Fluent
  SlackClient messageHandler(Handler<SlackMessage> handler);

  @Fluent
  SlackClient closeHandler(Handler<Void> handler);

  Set<String> channelIds();

  ChannelObject channel(String id);

  ChannelObject channelByName(String name);

  Set<String> imIds();

  IMObject im(String id);

  Set<String> userIds();

  SlackUser user(String id);

  SlackUser userByName(String name);

  void start(Handler<AsyncResult<Void>> handler);

  void getOrCreateIM(SlackUser user, Handler<AsyncResult<IMObject>> handler);

  void joinChannel(ChannelObject ch, Handler<AsyncResult<Void>> handler);

  void send(SlackChannel ch, String text);

  void send(SlackChannel ch, String text, Handler<AsyncResult<Void>> handler);

  void close();
}
