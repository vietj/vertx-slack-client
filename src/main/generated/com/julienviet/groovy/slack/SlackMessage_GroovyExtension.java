package com.julienviet.groovy.slack;
public class SlackMessage_GroovyExtension {
  public static java.util.Map<String, Object> from(com.julienviet.slack.SlackMessage j_receiver) {
    return j_receiver.from() != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.from().toJson()) : null;
  }
  public static java.util.Map<String, Object> channel(com.julienviet.slack.SlackMessage j_receiver) {
    return j_receiver.channel() != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.channel().toJson()) : null;
  }
}
