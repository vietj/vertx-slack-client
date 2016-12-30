package com.julienviet.slack;

import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SlackError extends RuntimeException {

  private final int code;

  public SlackError(JsonObject error) {
    this(error.getInteger("code"), error.getString("msg"));
  }

  public SlackError(int code, String msg) {
    super(msg);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
