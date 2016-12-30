require 'slack/slack_channel'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.ChannelObject
module Slack
  class ChannelObject < ::Slack::SlackChannel
    # @private
    # @param j_del [::Slack::ChannelObject] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::ChannelObject] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [String] text 
    # @return [void]
    def send(text=nil)
      if text.class == String && !block_given?
        return @j_del.java_method(:send, [Java::java.lang.String.java_class]).call(text)
      end
      raise ArgumentError, "Invalid arguments when calling send(text)"
    end
    # @return [String]
    def id
      if !block_given?
        return @j_del.java_method(:getId, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:getName, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    # @return [Fixnum]
    def created
      if !block_given?
        return @j_del.java_method(:getCreated, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling created()"
    end
    # @return [String]
    def creator
      if !block_given?
        return @j_del.java_method(:getCreator, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling creator()"
    end
    # @return [true,false]
    def archived?
      if !block_given?
        return @j_del.java_method(:isArchived, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling archived?()"
    end
    # @return [true,false]
    def general?
      if !block_given?
        return @j_del.java_method(:isGeneral, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling general?()"
    end
    # @return [Array<String>]
    def members
      if !block_given?
        return @j_del.java_method(:getMembers, []).call().to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling members()"
    end
    # @return [String]
    def topic_value
      if !block_given?
        return @j_del.java_method(:getTopicValue, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling topic_value()"
    end
    # @return [String]
    def topic_creator
      if !block_given?
        return @j_del.java_method(:getTopicCreator, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling topic_creator()"
    end
    # @return [Fixnum]
    def topic_last_set
      if !block_given?
        return @j_del.java_method(:getTopicLastSet, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling topic_last_set()"
    end
    # @return [String]
    def purpose_value
      if !block_given?
        return @j_del.java_method(:getPurposeValue, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling purpose_value()"
    end
    # @return [String]
    def purpose_creator
      if !block_given?
        return @j_del.java_method(:getPurposeCreator, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling purpose_creator()"
    end
    # @return [Fixnum]
    def purpose_last_set
      if !block_given?
        return @j_del.java_method(:getPurposeLastSet, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling purpose_last_set()"
    end
    # @return [true,false]
    def member?
      if !block_given?
        return @j_del.java_method(:isMember, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling member?()"
    end
    # @return [Fixnum]
    def unread_count
      if !block_given?
        return @j_del.java_method(:getUnreadCount, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling unread_count()"
    end
    # @return [Fixnum]
    def unread_count_display
      if !block_given?
        return @j_del.java_method(:getUnreadCountDisplay, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling unread_count_display()"
    end
  end
end
