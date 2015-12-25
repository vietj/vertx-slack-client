package io.nonobot.slack;

import io.nonobot.core.Config;
import io.nonobot.core.NonoBot;
import io.nonobot.core.adapter.*;
import io.nonobot.core.spi.AdapterFactory;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackAdapterFactory implements AdapterFactory {

  @Override
  public Adapter create(NonoBot bot, Config config) {
    String slackToken = config.getProperty("slack.token");
    if (slackToken != null) {
      return SlackAdapter.create(bot, new SlackOptions().setToken(slackToken));
    }
    return null;
  }
}
