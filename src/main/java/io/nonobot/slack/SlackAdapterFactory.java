package io.nonobot.slack;

import io.nonobot.core.Config;
import io.nonobot.core.adapter.*;
import io.nonobot.core.spi.BotAdapterFactory;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackAdapterFactory implements BotAdapterFactory {

  @Override
  public BotAdapter create(Config config) {
    String slackToken = config.getProperty("slack.token");
    if (slackToken != null) {
      return SlackAdapter.create(new SlackOptions().setToken(slackToken));
    }
    return null;
  }
}
