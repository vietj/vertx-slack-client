require 'slack/slack_channel'
require 'slack/slack_user'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.IMObject
module Slack
  class IMObject < ::Slack::SlackChannel
    # @private
    # @param j_del [::Slack::IMObject] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::IMObject] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [String] arg0 
    # @return [void]
    def send(arg0=nil)
      if arg0.class == String && !block_given?
        return @j_del.java_method(:send, [Java::java.lang.String.java_class]).call(arg0)
      end
      raise ArgumentError, "Invalid arguments when calling send(arg0)"
    end
    # @return [String]
    def id
      if !block_given?
        return @j_del.java_method(:getId, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    # @return [String]
    def user_id
      if !block_given?
        return @j_del.java_method(:userId, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling user_id()"
    end
    # @return [::Slack::SlackUser]
    def user
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:user, []).call(),::Slack::SlackUser)
      end
      raise ArgumentError, "Invalid arguments when calling user()"
    end
    # @return [Fixnum]
    def created
      if !block_given?
        return @j_del.java_method(:getCreated, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling created()"
    end
    # @return [true,false]
    def user_deleted?
      if !block_given?
        return @j_del.java_method(:isUserDeleted, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling user_deleted?()"
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
