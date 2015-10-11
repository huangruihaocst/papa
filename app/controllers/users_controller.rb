class UsersController < ApplicationController
  def current
    if current_user
      render json: { status: STATUS_SUCCESS, id: current_user.id.to_s }
    else
      json_failed
    end
  end
end