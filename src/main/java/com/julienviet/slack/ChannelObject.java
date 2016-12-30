package com.julienviet.slack;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ChannelObject extends SlackChannel {

  public ChannelObject(JsonObject json) {
    super(json);
  }

  public String getName() {
    return json.getString("name");
  }

  public long getCreated() {
    return json.getLong("created");
  }

  public String getCreator() {
    return json.getString("creator");
  }

  public boolean isArchived() {
    return json.getBoolean("is_archived");
  }

  public boolean isGeneral() {
    return json.getBoolean("is_general");
  }

  public List<String> getMembers() {
    return json.getJsonArray("members").getList();
  }

  public String getTopicValue() {
    return json.getJsonObject("topic").getString("value");
  }

  public String getTopicCreator() {
    return json.getJsonObject("topic").getString("creator");
  }

  public long getTopicLastSet() {
    return json.getJsonObject("topic").getLong("last_set");
  }

  public String getPurposeValue() {
    return json.getJsonObject("purpose").getString("value");
  }

  public String getPurposeCreator() {
    return json.getJsonObject("purpose").getString("creator");
  }

  public long getPurposeLastSet() {
    return json.getJsonObject("purpose").getLong("last_set");
  }

  public boolean isMember() {
    return json.getBoolean("is_member");
  }

  public int getUnreadCount() {
    return json.getInteger("unread_count");
  }

  public int getUnreadCountDisplay() {
    return json.getInteger("unread_count_display");
  }

}
