package com.julienviet.groovy.slack;
public class SlackClient_GroovyExtension {
  public static java.util.Map<String, Object> channel(com.julienviet.slack.SlackClient j_receiver, java.lang.String id) {
    return j_receiver.channel(id) != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.channel(id).toJson()) : null;
  }
  public static java.util.Map<String, Object> channelByName(com.julienviet.slack.SlackClient j_receiver, java.lang.String name) {
    return j_receiver.channelByName(name) != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.channelByName(name).toJson()) : null;
  }
  public static java.util.Map<String, Object> im(com.julienviet.slack.SlackClient j_receiver, java.lang.String id) {
    return j_receiver.im(id) != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.im(id).toJson()) : null;
  }
  public static java.util.Map<String, Object> user(com.julienviet.slack.SlackClient j_receiver, java.lang.String id) {
    return j_receiver.user(id) != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.user(id).toJson()) : null;
  }
  public static java.util.Map<String, Object> userByName(com.julienviet.slack.SlackClient j_receiver, java.lang.String name) {
    return j_receiver.userByName(name) != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(j_receiver.userByName(name).toJson()) : null;
  }
  public static void getOrCreateIM(com.julienviet.slack.SlackClient j_receiver, java.util.Map<String, Object> user, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> handler) {
    j_receiver.getOrCreateIM(user != null ? new com.julienviet.slack.SlackUser(io.vertx.core.impl.ConversionHelper.toJsonObject(user)) : null,
      handler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<com.julienviet.slack.IMObject>>() {
      public void handle(io.vertx.core.AsyncResult<com.julienviet.slack.IMObject> ar) {
        handler.handle(ar.map(event -> event != null ? io.vertx.core.impl.ConversionHelper.fromJsonObject(event.toJson()) : null));
      }
    } : null);
  }
  public static void joinChannel(com.julienviet.slack.SlackClient j_receiver, java.util.Map<String, Object> ch, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> handler) {
    j_receiver.joinChannel(ch != null ? new com.julienviet.slack.ChannelObject(io.vertx.core.impl.ConversionHelper.toJsonObject(ch)) : null,
      handler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        handler.handle(ar.map(event -> io.vertx.core.impl.ConversionHelper.fromObject(event)));
      }
    } : null);
  }
  public static void send(com.julienviet.slack.SlackClient j_receiver, java.util.Map<String, Object> ch, java.lang.String text) {
    j_receiver.send(ch != null ? new com.julienviet.slack.SlackChannel(io.vertx.core.impl.ConversionHelper.toJsonObject(ch)) : null,
      text);
  }
  public static void send(com.julienviet.slack.SlackClient j_receiver, java.util.Map<String, Object> ch, java.lang.String text, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> handler) {
    j_receiver.send(ch != null ? new com.julienviet.slack.SlackChannel(io.vertx.core.impl.ConversionHelper.toJsonObject(ch)) : null,
      text,
      handler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        handler.handle(ar.map(event -> io.vertx.core.impl.ConversionHelper.fromObject(event)));
      }
    } : null);
  }
}
