package com.julienviet.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class SlackUser {

  private JsonObject json;

  public SlackUser(JsonObject json) {
    this.json = json;
  }

  public SlackUser() {
    this.json = new JsonObject();
  }

  public String getId() {
    return json.getString("id");
  }

  public String getName() {
    return json.getString("name");
  }

  public JsonObject getProfile() {
    return json.getJsonObject("profile");
  }

  private boolean isAdmin() {
    return Boolean.TRUE.equals(json.getBoolean("is_admin"));
  }

  public boolean getHas2fa() {
    return Boolean.TRUE.equals(json.getBoolean("has2fa"));
  }

  public String getTwoFactoryType() {
    return json.getString("two_factor_type");
  }

  public boolean getHasFiles() {
    return Boolean.TRUE.equals(json.getBoolean("has_files"));
  }

  public JsonObject toJson() {
    return json.copy();
  }
}
