package com.julienviet.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class SlackChannel {

  protected final JsonObject json;

  public SlackChannel(JsonObject json) {
    this.json = json;
  }

  public String getId() {
    return json.getString("id");
  }

  public JsonObject toJson() {
    return json.copy();
  }

}
