package com.julienviet.groovy.slack;
public class SlackClient_GroovyStaticExtension {
  public static com.julienviet.slack.SlackClient create(com.julienviet.slack.SlackClient j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.core.impl.ConversionHelper.fromObject(com.julienviet.slack.SlackClient.create(vertx,
      options != null ? new com.julienviet.slack.SlackOptions(io.vertx.core.impl.ConversionHelper.toJsonObject(options)) : null));
  }
}
