package io.nonobot.slack;

import io.nonobot.core.Config;
import io.nonobot.core.adapter.*;
import io.nonobot.core.spi.BotAdapterFactory;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackAdapterFactory implements BotAdapterFactory {

  @Override
  public BotAdapter create(Vertx vertx, Config config) {
    String slackToken = config.getProperty("slack.token");
    if (slackToken != null) {
      return BotAdapter.create(vertx).requestHandler(SlackAdapter.create(new SlackOptions().setToken(slackToken)));
    }
    return null;
  }
}