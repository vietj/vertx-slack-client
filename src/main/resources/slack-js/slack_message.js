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

/** @module slack-js/slack_message */
var utils = require('vertx-js/util/utils');
var SlackUser = require('slack-js/slack_user');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSlackMessage = com.julienviet.slack.SlackMessage;

/**

 @class
*/
var SlackMessage = function(j_val) {

  var j_slackMessage = j_val;
  var that = this;

  /**

   @public

   @return {string}
   */
  this.channelId = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_slackMessage["channelId()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {SlackUser}
   */
  this.from = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_slackMessage["from()"](), SlackUser);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.text = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_slackMessage["text()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param body {string} 
   */
  this.reply = function(body) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_slackMessage["reply(java.lang.String)"](body);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_slackMessage;
};

// We export the Constructor function
module.exports = SlackMessage;