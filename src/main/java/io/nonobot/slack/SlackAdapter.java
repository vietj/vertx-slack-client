package io.nonobot.slack;

import io.nonobot.core.adapter.BotAdapter;
import io.nonobot.slack.impl.SlackAdapterImpl;
import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SlackAdapter extends BotAdapter {

  static SlackAdapter create(SlackOptions options) {
    return new SlackAdapterImpl(options);
  }
}
