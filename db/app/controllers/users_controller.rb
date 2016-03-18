class UsersController < ApplicationController
  def current
    user = check_login
    if user
      render json: { status: STATUS_SUCCESS, id: user.id.to_s }
    else
      json_failed(REASON_PERMISSION_DENIED)
    end
  end

  def update
    user = check_login
    if user.update(params.require(:user).permit(:phone, :email, :name, :password, :password_confirmation, :department, :class_name, :avator_id))
      json_successful
    else
      json_failed
    end
  end

  def show
    begin
      @user = User.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end
end