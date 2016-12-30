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

/** @module slack-js/im_object */
var utils = require('vertx-js/util/utils');
var SlackChannel = require('slack-js/slack_channel');
var SlackUser = require('slack-js/slack_user');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JIMObject = com.julienviet.slack.IMObject;

/**

 @class
*/
var IMObject = function(j_val) {

  var j_iMObject = j_val;
  var that = this;
  SlackChannel.call(this, j_val);

  /**

   @public
   @param arg0 {string} 
   */
  this.send = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_iMObject["send(java.lang.String)"](arg0);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_iMObject["id()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.userId = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_iMObject["userId()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {SlackUser}
   */
  this.user = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_iMObject["user()"](), SlackUser);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.created = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_iMObject["created()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean}
   */
  this.isUserDeleted = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_iMObject["isUserDeleted()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_iMObject["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_iMObject;
};

// We export the Constructor function
module.exports = IMObject;