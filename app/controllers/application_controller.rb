class ApplicationController < ActionController::Base
  class TokenException < Exception
    def initialize(msg)
      @msg = msg
    end
    def message
      @msg
    end
  end

  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  #protect_from_forgery with: :exception

  before_action :configure_permitted_parameters, if: :devise_controller?

  # the following class macro and two methods are token authentication helpers
  rescue_from TokenException, with: :invalid_token
  def invalid_token(except)
    json_failed(except.message)
  end
  # raise TokenException if authentication failure occurs
  def check_token(token_str, user_id, is_teacher = false)
    raise TokenException.new(REASON_TOKEN_INVALID) unless user_id

    if token_str
      token = Token.find_by_token(token_str)
      # deny when is_teacher is required but the token is not a teacher
      if token && token.user_id == user_id.to_i && (!token.user.is_teacher && is_teacher)
        if Time.now > token.valid_until
          raise TokenException.new(REASON_TOKEN_TIMEOUT)
        end
      else
        raise TokenException.new(REASON_TOKEN_INVALID)
      end
    elsif current_user
      if current_user.id != user_id.to_i
        raise TokenException.new(REASON_TOKEN_INVALID)
      end
    else
      raise TokenException.new(REASON_TOKEN_INVALID)
    end
  end

  def check_token_type(token_str, type)

  end

  include ApplicationHelper::StatusRenderingHelpers

  protected

  def configure_permitted_parameters
    devise_parameter_sanitizer.for(:sign_up) { |u| u.permit(:phone, :name, :email, :password, :password_confirmation, :remember_me) }
    devise_parameter_sanitizer.for(:sign_in) { |u| u.permit(:login, :username, :email, :password, :remember_me) }
    devise_parameter_sanitizer.for(:account_update) { |u| u.permit(:username, :email, :password, :password_confirmation, :current_password) }
  end

end
