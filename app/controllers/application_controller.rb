class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  def json_failed(reason = nil)
    if reason
      render json: { status: 'failed', reason: reason }
    else
      render json: { status: 'failed' }
    end
  end
  def json_successful
    render json: { status: 'successful' }
  end

  def html_failed(reason = nil)
    redirect_to '/404.html'
  end

end
