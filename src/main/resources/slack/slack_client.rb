require 'slack/channel_object'
require 'slack/slack_user'
require 'vertx/vertx'
require 'slack/im_object'
require 'slack/slack_message'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.SlackClient
module Slack
  class SlackClient
    # @private
    # @param j_del [::Slack::SlackClient] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::SlackClient] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [Hash] options 
    # @return [::Slack::SlackClient]
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::ComJulienvietSlack::SlackClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::ComJulienvietSlack::SlackOptions.java_class]).call(vertx.j_del,Java::ComJulienvietSlack::SlackOptions.new(::Vertx::Util::Utils.to_json_object(options))),::Slack::SlackClient)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,options)"
    end
    # @yield 
    # @return [self]
    def message_handler
      if block_given?
        @j_del.java_method(:messageHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::Slack::SlackMessage)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling message_handler()"
    end
    # @yield 
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    # @return [Set<String>]
    def channel_ids
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:channelIds, []).call()).map! { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling channel_ids()"
    end
    # @param [String] id 
    # @return [::Slack::ChannelObject]
    def channel(id=nil)
      if id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:channel, [Java::java.lang.String.java_class]).call(id),::Slack::ChannelObject)
      end
      raise ArgumentError, "Invalid arguments when calling channel(id)"
    end
    # @param [String] name 
    # @return [::Slack::ChannelObject]
    def channel_by_name(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:channelByName, [Java::java.lang.String.java_class]).call(name),::Slack::ChannelObject)
      end
      raise ArgumentError, "Invalid arguments when calling channel_by_name(name)"
    end
    # @return [Set<String>]
    def im_ids
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:imIds, []).call()).map! { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling im_ids()"
    end
    # @param [String] id 
    # @return [::Slack::IMObject]
    def im(id=nil)
      if id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:im, [Java::java.lang.String.java_class]).call(id),::Slack::IMObject)
      end
      raise ArgumentError, "Invalid arguments when calling im(id)"
    end
    # @return [Set<String>]
    def user_ids
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:userIds, []).call()).map! { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling user_ids()"
    end
    # @param [String] id 
    # @return [::Slack::SlackUser]
    def user(id=nil)
      if id.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:user, [Java::java.lang.String.java_class]).call(id),::Slack::SlackUser)
      end
      raise ArgumentError, "Invalid arguments when calling user(id)"
    end
    # @yield 
    # @return [void]
    def start
      if block_given?
        return @j_del.java_method(:start, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling start()"
    end
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
