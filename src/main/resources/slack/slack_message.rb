require 'slack/slack_user'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.SlackMessage
module Slack
  class SlackMessage
    # @private
    # @param j_del [::Slack::SlackMessage] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::SlackMessage] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [String]
    def channel_id
      if !block_given?
        return @j_del.java_method(:channelId, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling channel_id()"
    end
    # @return [::Slack::SlackUser]
    def from
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:from, []).call(),::Slack::SlackUser)
      end
      raise ArgumentError, "Invalid arguments when calling from()"
    end
    # @return [String]
    def text
      if !block_given?
        return @j_del.java_method(:text, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling text()"
    end
    # @param [String] body 
    # @return [void]
    def reply(body=nil)
      if body.class == String && !block_given?
        return @j_del.java_method(:reply, [Java::java.lang.String.java_class]).call(body)
      end
      raise ArgumentError, "Invalid arguments when calling reply(body)"
    end
  end
end
