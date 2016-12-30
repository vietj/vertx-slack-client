package com.julienviet.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class IMObject extends SlackChannel {

  public IMObject(JsonObject json) {
    super(json);
  }

  public String getUserId() {
    return json.getString("user");
  }

  public long getCreated() {
    return json.getLong("created");
  }

  public boolean isUserDeleted() {
    return json.getBoolean("is_user_deleted");
  }
}
