package io.nonobot.slack;

import io.nonobot.core.adapter.ConnectionRequest;
import io.nonobot.slack.impl.SlackAdapterImpl;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SlackAdapter extends Handler<ConnectionRequest> {

  static SlackAdapter create(SlackOptions options) {
    return new SlackAdapterImpl(options);
  }
}
