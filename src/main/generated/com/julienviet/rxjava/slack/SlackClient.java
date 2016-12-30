/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.julienviet.rxjava.slack;

import java.util.Map;
import rx.Observable;
import rx.Single;
import com.julienviet.slack.ChannelObject;
import com.julienviet.slack.SlackChannel;
import com.julienviet.slack.SlackUser;
import io.vertx.rxjava.core.Vertx;
import java.util.Set;
import com.julienviet.slack.IMObject;
import com.julienviet.slack.SlackOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link com.julienviet.slack.SlackClient original} non RX-ified interface using Vert.x codegen.
 */

@io.vertx.lang.rxjava.RxGen(com.julienviet.slack.SlackClient.class)
public class SlackClient {

  public static final io.vertx.lang.rxjava.TypeArg<SlackClient> __TYPE_ARG = new io.vertx.lang.rxjava.TypeArg<>(
    obj -> new SlackClient((com.julienviet.slack.SlackClient) obj),
    SlackClient::getDelegate
  );

  private final com.julienviet.slack.SlackClient delegate;
  
  public SlackClient(com.julienviet.slack.SlackClient delegate) {
    this.delegate = delegate;
  }

  public com.julienviet.slack.SlackClient getDelegate() {
    return delegate;
  }

  public static SlackClient create(Vertx vertx, SlackOptions options) { 
    SlackClient ret = SlackClient.newInstance(com.julienviet.slack.SlackClient.create(vertx.getDelegate(), options));
    return ret;
  }

  public SlackClient messageHandler(Handler<SlackMessage> handler) { 
    delegate.messageHandler(new Handler<com.julienviet.slack.SlackMessage>() {
      public void handle(com.julienviet.slack.SlackMessage event) {
        handler.handle(SlackMessage.newInstance(event));
      }
    });
    return this;
  }

  public SlackClient closeHandler(Handler<Void> handler) { 
    delegate.closeHandler(handler);
    return this;
  }

  public Set<String> channelIds() { 
    Set<String> ret = delegate.channelIds();
    return ret;
  }

  public ChannelObject channel(String id) { 
    ChannelObject ret = delegate.channel(id);
    return ret;
  }

  public ChannelObject channelByName(String name) { 
    ChannelObject ret = delegate.channelByName(name);
    return ret;
  }

  public Set<String> imIds() { 
    Set<String> ret = delegate.imIds();
    return ret;
  }

  public IMObject im(String id) { 
    IMObject ret = delegate.im(id);
    return ret;
  }

  public Set<String> userIds() { 
    Set<String> ret = delegate.userIds();
    return ret;
  }

  public SlackUser user(String id) { 
    SlackUser ret = delegate.user(id);
    return ret;
  }

  public SlackUser userByName(String name) { 
    SlackUser ret = delegate.userByName(name);
    return ret;
  }

  public void start(Handler<AsyncResult<Void>> handler) { 
    delegate.start(handler);
  }

  public Single<Void> rxStart() { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      start(fut);
    }));
  }

  public void getOrCreateIM(SlackUser user, Handler<AsyncResult<IMObject>> handler) { 
    delegate.getOrCreateIM(user, handler);
  }

  public Single<IMObject> rxGetOrCreateIM(SlackUser user) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      getOrCreateIM(user, fut);
    }));
  }

  public void joinChannel(ChannelObject ch, Handler<AsyncResult<Void>> handler) { 
    delegate.joinChannel(ch, handler);
  }

  public Single<Void> rxJoinChannel(ChannelObject ch) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      joinChannel(ch, fut);
    }));
  }

  public void send(SlackChannel ch, String text) { 
    delegate.send(ch, text);
  }

  public void send(SlackChannel ch, String text, Handler<AsyncResult<Void>> handler) { 
    delegate.send(ch, text, handler);
  }

  public Single<Void> rxSend(SlackChannel ch, String text) { 
    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {
      send(ch, text, fut);
    }));
  }

  public void close() { 
    delegate.close();
  }


  public static SlackClient newInstance(com.julienviet.slack.SlackClient arg) {
    return arg != null ? new SlackClient(arg) : null;
  }
}
