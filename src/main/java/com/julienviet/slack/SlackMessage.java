package com.julienviet.slack;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SlackMessage {

  SlackUser from();

  String text();

  SlackChannel channel();

}
