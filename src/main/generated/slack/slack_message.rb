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
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == SlackMessage
    end
    def @@j_api_type.wrap(obj)
      SlackMessage.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::ComJulienvietSlack::SlackMessage.java_class
    end
    # @return [Hash]
    def from
      if !block_given?
        return @j_del.java_method(:from, []).call() != nil ? JSON.parse(@j_del.java_method(:from, []).call().toJson.encode) : nil
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
    # @return [Hash]
    def channel
      if !block_given?
        return @j_del.java_method(:channel, []).call() != nil ? JSON.parse(@j_del.java_method(:channel, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling channel()"
    end
  end
end
