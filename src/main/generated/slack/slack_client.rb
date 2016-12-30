require 'vertx/vertx'
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
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == SlackClient
    end
    def @@j_api_type.wrap(obj)
      SlackClient.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::ComJulienvietSlack::SlackClient.java_class
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [Hash] options 
    # @return [::Slack::SlackClient]
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::ComJulienvietSlack::SlackClient.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::ComJulienvietSlack::SlackOptions.java_class]).call(vertx.j_del,Java::ComJulienvietSlack::SlackOptions.new(::Vertx::Util::Utils.to_json_object(options))),::Slack::SlackClient)
      end
      raise ArgumentError, "Invalid arguments when calling create(#{vertx},#{options})"
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
    # @return [Hash]
    def channel(id=nil)
      if id.class == String && !block_given?
        return @j_del.java_method(:channel, [Java::java.lang.String.java_class]).call(id) != nil ? JSON.parse(@j_del.java_method(:channel, [Java::java.lang.String.java_class]).call(id).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling channel(#{id})"
    end
    # @param [String] name 
    # @return [Hash]
    def channel_by_name(name=nil)
      if name.class == String && !block_given?
        return @j_del.java_method(:channelByName, [Java::java.lang.String.java_class]).call(name) != nil ? JSON.parse(@j_del.java_method(:channelByName, [Java::java.lang.String.java_class]).call(name).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling channel_by_name(#{name})"
    end
    # @return [Set<String>]
    def im_ids
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:imIds, []).call()).map! { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling im_ids()"
    end
    # @param [String] id 
    # @return [Hash]
    def im(id=nil)
      if id.class == String && !block_given?
        return @j_del.java_method(:im, [Java::java.lang.String.java_class]).call(id) != nil ? JSON.parse(@j_del.java_method(:im, [Java::java.lang.String.java_class]).call(id).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling im(#{id})"
    end
    # @return [Set<String>]
    def user_ids
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:userIds, []).call()).map! { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling user_ids()"
    end
    # @param [String] id 
    # @return [Hash]
    def user(id=nil)
      if id.class == String && !block_given?
        return @j_del.java_method(:user, [Java::java.lang.String.java_class]).call(id) != nil ? JSON.parse(@j_del.java_method(:user, [Java::java.lang.String.java_class]).call(id).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling user(#{id})"
    end
    # @param [String] name 
    # @return [Hash]
    def user_by_name(name=nil)
      if name.class == String && !block_given?
        return @j_del.java_method(:userByName, [Java::java.lang.String.java_class]).call(name) != nil ? JSON.parse(@j_del.java_method(:userByName, [Java::java.lang.String.java_class]).call(name).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling user_by_name(#{name})"
    end
    # @yield 
    # @return [void]
    def start
      if block_given?
        return @j_del.java_method(:start, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling start()"
    end
    # @param [Hash] user 
    # @yield 
    # @return [void]
    def get_or_create_im(user=nil)
      if user.class == Hash && block_given?
        return @j_del.java_method(:getOrCreateIM, [Java::ComJulienvietSlack::SlackUser.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::ComJulienvietSlack::SlackUser.new(::Vertx::Util::Utils.to_json_object(user)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_or_create_im(#{user})"
    end
    # @param [Hash] ch 
    # @yield 
    # @return [void]
    def join_channel(ch=nil)
      if ch.class == Hash && block_given?
        return @j_del.java_method(:joinChannel, [Java::ComJulienvietSlack::ChannelObject.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::ComJulienvietSlack::ChannelObject.new(::Vertx::Util::Utils.to_json_object(ch)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling join_channel(#{ch})"
    end
    # @param [Hash] ch 
    # @param [String] text 
    # @yield 
    # @return [void]
    def send(ch=nil,text=nil)
      if ch.class == Hash && text.class == String && !block_given?
        return @j_del.java_method(:send, [Java::ComJulienvietSlack::SlackChannel.java_class,Java::java.lang.String.java_class]).call(Java::ComJulienvietSlack::SlackChannel.new(::Vertx::Util::Utils.to_json_object(ch)),text)
      elsif ch.class == Hash && text.class == String && block_given?
        return @j_del.java_method(:send, [Java::ComJulienvietSlack::SlackChannel.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::ComJulienvietSlack::SlackChannel.new(::Vertx::Util::Utils.to_json_object(ch)),text,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling send(#{ch},#{text})"
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
