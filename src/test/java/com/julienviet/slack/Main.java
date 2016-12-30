package com.julienviet.slack;

import io.vertx.core.Vertx;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) throws Exception {

    Properties props = new Properties();
    props.load(new FileInputStream("test.properties"));

    Vertx vertx = Vertx.vertx();
    SlackClient client = SlackClient.create(vertx, new SlackOptions().setToken(props.getProperty("SLACK_TOKEN")));

    client.messageHandler(msg -> {
      System.out.println("Got message " + msg.channel().getId() + " " + msg.text());
    });

    client.start(ar -> {
      if (ar.succeeded()) {
        System.out.println("Bot connected");
/*
        for (String userId : client.userIds()) {
          System.out.println("1 " + userId + " : " + client.user(userId).name());
        }
        for (String channelId : client.channelIds()) {
          System.out.println("2 " + channelId + " : " + client.channel(channelId).name() + " / " + client.channel(channelId).isMember());
        }
        for (String imId : client.imIds()) {
          System.out.println("3 " + imId + " : " + client.im(imId).user().name());
        }
*/
        client.joinChannel(client.channelByName("halte-aux-channels"), ar2 -> {
          System.out.println(ar2.succeeded());
        });
/*
        client.channelByName("nonotest").send("test", ar2 -> {
          System.out.println("nonotest " + ar2.succeeded());
        });
        client.userByName("julien").getOrCreateIM(ar2 -> {
          System.out.println("sending " + ar2.succeeded());
          if (ar2.succeeded()) {
            ar2.result().send("Hello Julien!", ar3 -> {
              System.out.println("ACKED");
            });
          } else {
            ar2.cause().printStackTrace();
          }
        });
*/
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}
