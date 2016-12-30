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
import com.julienviet.slack.SlackChannel;
import com.julienviet.slack.SlackUser;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link com.julienviet.slack.SlackMessage original} non RX-ified interface using Vert.x codegen.
 */

@io.vertx.lang.rxjava.RxGen(com.julienviet.slack.SlackMessage.class)
public class SlackMessage {

  public static final io.vertx.lang.rxjava.TypeArg<SlackMessage> __TYPE_ARG = new io.vertx.lang.rxjava.TypeArg<>(
    obj -> new SlackMessage((com.julienviet.slack.SlackMessage) obj),
    SlackMessage::getDelegate
  );

  private final com.julienviet.slack.SlackMessage delegate;
  
  public SlackMessage(com.julienviet.slack.SlackMessage delegate) {
    this.delegate = delegate;
  }

  public com.julienviet.slack.SlackMessage getDelegate() {
    return delegate;
  }

  public SlackUser from() { 
    SlackUser ret = delegate.from();
    return ret;
  }

  public String text() { 
    String ret = delegate.text();
    return ret;
  }

  public SlackChannel channel() { 
    SlackChannel ret = delegate.channel();
    return ret;
  }


  public static SlackMessage newInstance(com.julienviet.slack.SlackMessage arg) {
    return arg != null ? new SlackMessage(arg) : null;
  }
}
