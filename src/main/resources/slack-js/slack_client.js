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
var ChannelObject = require('slack-js/channel_object');
var SlackUser = require('slack-js/slack_user');
var Vertx = require('vertx-js/vertx');
var IMObject = require('slack-js/im_object');
var SlackMessage = require('slack-js/slack_message');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSlackClient = com.julienviet.slack.SlackClient;
var SlackOptions = com.julienviet.slack.SlackOptions;

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
      handler(utils.convReturnVertxGen(jVal, SlackMessage));
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
   @return {ChannelObject}
   */
  this.channel = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_slackClient["channel(java.lang.String)"](id), ChannelObject);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param name {string} 
   @return {ChannelObject}
   */
  this.channelByName = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_slackClient["channelByName(java.lang.String)"](name), ChannelObject);
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
   @return {IMObject}
   */
  this.im = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_slackClient["im(java.lang.String)"](id), IMObject);
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
   @return {SlackUser}
   */
  this.user = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_slackClient["user(java.lang.String)"](id), SlackUser);
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

/**

 @memberof module:slack-js/slack_client
 @param vertx {Vertx} 
 @param options {Object} 
 @return {SlackClient}
 */
SlackClient.create = function(vertx, options) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JSlackClient["create(io.vertx.core.Vertx,com.julienviet.slack.SlackOptions)"](vertx._jdel, options != null ? new SlackOptions(new JsonObject(JSON.stringify(options))) : null), SlackClient);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = SlackClient;