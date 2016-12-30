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

/** @module slack-js/channel_object */
var utils = require('vertx-js/util/utils');
var SlackChannel = require('slack-js/slack_channel');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JChannelObject = com.julienviet.slack.ChannelObject;

/**

 @class
*/
var ChannelObject = function(j_val) {

  var j_channelObject = j_val;
  var that = this;
  SlackChannel.call(this, j_val);

  /**

   @public
   @param text {string} 
   */
  this.send = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_channelObject["send(java.lang.String)"](text);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["id()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.created = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["created()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.creator = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["creator()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean}
   */
  this.isArchived = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["isArchived()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean}
   */
  this.isGeneral = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["isGeneral()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<string>}
   */
  this.members = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["members()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.topicValue = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["topicValue()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.topicCreator = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["topicCreator()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.topicLastSet = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["topicLastSet()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.purposeValue = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["purposeValue()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.purposeCreator = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["purposeCreator()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.purposeLastSet = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["purposeLastSet()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean}
   */
  this.isMember = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["isMember()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.unreadCount = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["unreadCount()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number}
   */
  this.unreadCountDisplay = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_channelObject["unreadCountDisplay()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_channelObject;
};

// We export the Constructor function
module.exports = ChannelObject;