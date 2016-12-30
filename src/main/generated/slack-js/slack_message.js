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

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSlackMessage = Java.type('com.julienviet.slack.SlackMessage');
var SlackChannel = Java.type('com.julienviet.slack.SlackChannel');
var SlackUser = Java.type('com.julienviet.slack.SlackUser');

/**

 @class
*/
var SlackMessage = function(j_val) {

  var j_slackMessage = j_val;
  var that = this;

  /**

   @public

   @return {Object}
   */
  this.from = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnDataObject(j_slackMessage["from()"]());
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

   @return {Object}
   */
  this.channel = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnDataObject(j_slackMessage["channel()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_slackMessage;
};

SlackMessage._jclass = utils.getJavaClass("com.julienviet.slack.SlackMessage");
SlackMessage._jtype = {
  accept: function(obj) {
    return SlackMessage._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(SlackMessage.prototype, {});
    SlackMessage.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
SlackMessage._create = function(jdel) {
  var obj = Object.create(SlackMessage.prototype, {});
  SlackMessage.apply(obj, arguments);
  return obj;
}
module.exports = SlackMessage;