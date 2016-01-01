require 'nonobot/connection_request'
require 'vertx/util/utils.rb'
# Generated from io.nonobot.slack.SlackAdapter
module NonobotSlack
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class SlackAdapter
    # @private
    # @param j_del [::NonobotSlack::SlackAdapter] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::NonobotSlack::SlackAdapter] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Nonobot::ConnectionRequest] arg0 
    # @return [void]
    def handle(arg0=nil)
      if arg0.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:handle, [Java::IoNonobotCoreAdapter::ConnectionRequest.java_class]).call(arg0.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling handle(arg0)"
    end
    # @param [Hash] options 
    # @return [::NonobotSlack::SlackAdapter]
    def self.create(options=nil)
      if options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoNonobotSlack::SlackAdapter.java_method(:create, [Java::IoNonobotSlack::SlackOptions.java_class]).call(Java::IoNonobotSlack::SlackOptions.new(::Vertx::Util::Utils.to_json_object(options))),::NonobotSlack::SlackAdapter)
      end
      raise ArgumentError, "Invalid arguments when calling create(options)"
    end
  end
end
