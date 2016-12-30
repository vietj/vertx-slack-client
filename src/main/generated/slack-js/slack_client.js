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

/** @module slack-js/slack_client */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var SlackMessage = require('slack-js/slack_message');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSlackClient = Java.type('com.julienviet.slack.SlackClient');
var ChannelObject = Java.type('com.julienviet.slack.ChannelObject');
var SlackChannel = Java.type('com.julienviet.slack.SlackChannel');
var SlackUser = Java.type('com.julienviet.slack.SlackUser');
var IMObject = Java.type('com.julienviet.slack.IMObject');
var SlackOptions = Java.type('com.julienviet.slack.SlackOptions');

/**

 @class
*/
var SlackClient = function(j_val) {

  var j_slackClient = j_val;
  var that = this;

  /**

   @public
   @param handler {function} 
   @return {SlackClient}
   */
  this.messageHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_slackClient["messageHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(SlackMessage, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {SlackClient}
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_slackClient["closeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<string>}
   */
  this.channelIds = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnSet(j_slackClient["channelIds()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @return {Object}
   */
  this.channel = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnDataObject(j_slackClient["channel(java.lang.String)"](id));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param name {string} 
   @return {Object}
   */
  this.channelByName = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnDataObject(j_slackClient["channelByName(java.lang.String)"](name));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<string>}
   */
  this.imIds = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnSet(j_slackClient["imIds()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @return {Object}
   */
  this.im = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnDataObject(j_slackClient["im(java.lang.String)"](id));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<string>}
   */
  this.userIds = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnSet(j_slackClient["userIds()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {string} 
   @return {Object}
   */
  this.user = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnDataObject(j_slackClient["user(java.lang.String)"](id));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param name {string} 
   @return {Object}
   */
  this.userByName = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnDataObject(j_slackClient["userByName(java.lang.String)"](name));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   */
  this.start = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_slackClient["start(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param user {Object} 
   @param handler {function} 
   */
  this.getOrCreateIM = function(user, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_slackClient["getOrCreateIM(com.julienviet.slack.SlackUser,io.vertx.core.Handler)"](user != null ? new SlackUser(new JsonObject(Java.asJSONCompatible(user))) : null, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnDataObject(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ch {Object} 
   @param handler {function} 
   */
  this.joinChannel = function(ch, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_slackClient["joinChannel(com.julienviet.slack.ChannelObject,io.vertx.core.Handler)"](ch != null ? new ChannelObject(new JsonObject(Java.asJSONCompatible(ch))) : null, function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ch {Object} 
   @param text {string} 
   @param handler {function} 
   */
  this.send = function() {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'string') {
      j_slackClient["send(com.julienviet.slack.SlackChannel,java.lang.String)"](__args[0] != null ? new SlackChannel(new JsonObject(Java.asJSONCompatible(__args[0]))) : null, __args[1]);
    }  else if (__args.length === 3 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_slackClient["send(com.julienviet.slack.SlackChannel,java.lang.String,io.vertx.core.Handler)"](__args[0] != null ? new SlackChannel(new JsonObject(Java.asJSONCompatible(__args[0]))) : null, __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_slackClient["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_slackClient;
};

SlackClient._jclass = utils.getJavaClass("com.julienviet.slack.SlackClient");
SlackClient._jtype = {
  accept: function(obj) {
    return SlackClient._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(SlackClient.prototype, {});
    SlackClient.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
SlackClient._create = function(jdel) {
  var obj = Object.create(SlackClient.prototype, {});
  SlackClient.apply(obj, arguments);
  return obj;
}
/**

 @memberof module:slack-js/slack_client
 @param vertx {Vertx} 
 @param options {Object} 
 @return {SlackClient}
 */
SlackClient.create = function(vertx, options) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(SlackClient, JSlackClient["create(io.vertx.core.Vertx,com.julienviet.slack.SlackOptions)"](vertx._jdel, options != null ? new SlackOptions(new JsonObject(Java.asJSONCompatible(options))) : null));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = SlackClient;