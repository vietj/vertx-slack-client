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

/** @module nonobot-slack-js/slack_adapter */
var utils = require('vertx-js/util/utils');
var BotAdapter = require('nonobot-js/bot_adapter');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSlackAdapter = io.nonobot.slack.SlackAdapter;
var SlackOptions = io.nonobot.slack.SlackOptions;

/**

 @class
*/
var SlackAdapter = function(j_val) {

  var j_slackAdapter = j_val;
  var that = this;
  BotAdapter.call(this, j_val);

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_slackAdapter;
};

/**

 @memberof module:nonobot-slack-js/slack_adapter
 @param options {Object} 
 @return {SlackAdapter}
 */
SlackAdapter.create = function(options) {
  var __args = arguments;
  if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
    return utils.convReturnVertxGen(JSlackAdapter["create(io.nonobot.slack.SlackOptions)"](options != null ? new SlackOptions(new JsonObject(JSON.stringify(options))) : null), SlackAdapter);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = SlackAdapter;