require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.SlackChannel
module Slack
  class SlackChannel
    # @private
    # @param j_del [::Slack::SlackChannel] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::SlackChannel] the underlying java delegate
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
  end
end
