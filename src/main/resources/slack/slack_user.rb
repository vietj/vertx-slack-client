require 'slack/im_object'
require 'vertx/util/utils.rb'
# Generated from com.julienviet.slack.SlackUser
module Slack
  class SlackUser
    # @private
    # @param j_del [::Slack::SlackUser] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::Slack::SlackUser] the underlying java delegate
    def j_del
      @j_del
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
    # @return [Hash{String => Object}]
    def profile
      if !block_given?
        return @j_del.java_method(:profile, []).call() != nil ? JSON.parse(@j_del.java_method(:profile, []).call().encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling profile()"
    end
    # @return [true,false]
    def admin?
      if !block_given?
        return @j_del.java_method(:isAdmin, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling admin?()"
    end
    # @return [true,false]
    def owner?
      if !block_given?
        return @j_del.java_method(:isOwner, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling owner?()"
    end
    # @return [true,false]
    def primary_owner?
      if !block_given?
        return @j_del.java_method(:isPrimaryOwner, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling primary_owner?()"
    end
    # @return [true,false]
    def restricted?
      if !block_given?
        return @j_del.java_method(:isRestricted, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling restricted?()"
    end
    # @return [true,false]
    def ultra_restricted?
      if !block_given?
        return @j_del.java_method(:isUltraRestricted, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling ultra_restricted?()"
    end
    # @return [true,false]
    def has2fa?
      if !block_given?
        return @j_del.java_method(:has2fa, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling has2fa?()"
    end
    # @return [String]
    def two_factor_type
      if !block_given?
        return @j_del.java_method(:twoFactorType, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling two_factor_type()"
    end
    # @return [true,false]
    def has_files?
      if !block_given?
        return @j_del.java_method(:hasFiles, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling has_files?()"
    end
    # @yield 
    # @return [void]
    def get_or_create_im
      if block_given?
        return @j_del.java_method(:getOrCreateIM, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Slack::IMObject) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_or_create_im()"
    end
  end
end
