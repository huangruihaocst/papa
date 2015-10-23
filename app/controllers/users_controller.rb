class UsersController < ApplicationController
  def current
    user = check_login
    if user
      render json: { status: STATUS_SUCCESS, id: user.id.to_s }
    else
      json_failed(REASON_PERMISSION_DENIED)
    end
  end
end