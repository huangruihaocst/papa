class ApplicationController < ActionController::Base
  class TokenException < Exception
    def initialize(msg)
      @msg = msg
    end
    def message
      @msg
    end
  end
  class RequestException < Exception
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
  rescue_from RequestException, with: :request_error
  rescue_from ActiveRecord::RecordNotFound, with: :resource_not_found

  def invalid_token(except)
    json_failed(except.message)
  end
  def request_error(except)
    json_failed(except.message)
  end
  def resource_not_found(except)
    json_failed(REASON_RESOURCE_NOT_FOUND)
  end

  # raise TokenException if authentication failure occurs
  def check_token(user_id, token_str = nil, teacher_required = false)
    raise TokenException.new(REASON_TOKEN_INVALID) unless user_id

    if !token_str && params[:token]
      token_str = params[:token]
    end

    if token_str
      token = Token.find_by_token(token_str)
      # deny when teacher_required is required but the token is not a teacher
      raise TokenException.new(REASON_TOKEN_INVALID) unless token && token.user_id == user_id.to_i
      raise TokenException.new(REASON_TOKEN_TIMEOUT) unless token.token_valid?
      raise TokenException.new(REASON_PERMISSION_DENIED) unless !teacher_required || token.user.is_teacher
    elsif current_user
      raise TokenException.new(REASON_PERMISSION_DENIED) unless current_user.id == user_id.to_i
      raise TokenException.new(REASON_PERMISSION_DENIED) unless !teacher_required || current_user.is_teacher
    else
      raise TokenException.new(REASON_TOKEN_INVALID)
    end
    true
  end

  def check_login
    if params[:token]
      begin
        token = Token.find_by_token(params[:token])
      rescue ActiveRecord::RecordNotFound
        raise TokenException.new(REASON_TOKEN_INVALID)
      end
      raise TokenException.new(REASON_TOKEN_INVALID) unless token && token.user.is_a?(User) && token.token_valid?
      token.user
    elsif current_user
      current_user
    else
      raise TokenException.new(REASON_TOKEN_INVALID)
    end
  end

  def check_admin
    if params[:token]
      begin
        token = Token.find_by_token(params[:token])
      rescue ActiveRecord::RecordNotFound
        raise TokenException.new(REASON_TOKEN_INVALID)
      end
      raise TokenException.new(REASON_TOKEN_INVALID) unless token && token.user.is_a?(User)
      raise TokenException.new(REASON_TOKEN_TIMEOUT) unless token.token_valid?
      raise TokenException.new(REASON_PERMISSION_DENIED) unless token.user.is_admin?
      token.user
    elsif current_user
      raise TokenException.new(REASON_PERMISSION_DENIED) unless current_user.is_admin?
      current_user
    else
      raise TokenException.new(REASON_TOKEN_INVALID)
    end
  end

  def check_teacher
    if params[:token]
      token = Token.find_by_token(params[:token])
      raise TokenException.new(REASON_TOKEN_INVALID) unless token && token.user.is_a?(User) && token.token_valid?
      raise TokenException.new(REASON_PERMISSION_DENIED) unless token.user.is_teacher?
      token.user
    elsif current_user
      raise TokenException.new(REASON_PERMISSION_DENIED) unless current_user.is_teacher?
      current_user
    else
      raise TokenException.new(REASON_TOKEN_INVALID)
    end
  end

  def must_be_a_teacher_of(token, course)
    raise TokenException.new(REASON_TOKEN_INVALID) unless course && course.is_a?(Course)

    if token
      begin
        token = Token.find_by_token(token)
        raise TokenException.new(REASON_TOKEN_INVALID) unless token
        user = token.user
      rescue
        raise TokenException.new(REASON_TOKEN_INVALID)
      end
    else
      raise TokenException.new(REASON_TOKEN_INVALID) unless current_user
      user = current_user
    end

    raise TokenException.new(REASON_PERMISSION_DENIED) unless course.teachers.include?(user)
    true
  end

  include ApplicationHelper::StatusRenderingHelpers

  protected

  def configure_permitted_parameters
    devise_parameter_sanitizer.for(:sign_up) { |u| u.permit(:phone, :name, :email, :password, :password_confirmation, :remember_me) }
    devise_parameter_sanitizer.for(:sign_in) { |u| u.permit(:login, :username, :email, :password, :remember_me) }
    devise_parameter_sanitizer.for(:account_update) { |u| u.permit(:username, :email, :password, :password_confirmation, :current_password) }
  end

end
