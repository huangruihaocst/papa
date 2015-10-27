module Users
  class SessionsController < Devise::SessionsController
    before_filter :configure_sign_in_params, only: [:create]

    # #GET /resource/sign_in
    # def new
    #   super
    # end

    #POST /users/sign_in
    def create
      respond_to do |format|
        format.html { super }
        format.json do
          @user = warden.authenticate(auth_options)
          if @user
            sign_in(resource_name, @user)
            token = current_user.create_token
            render json: { status: STATUS_SUCCESS, token: token.token, id: @user.id, is_teacher: @user.is_teacher?, is_admin: @user.is_admin? }
          else
            render json: { status: STATUS_FAIL, reason: REASON_INVALID_FIELD }
          end
        end
      end
    end

    # DELETE /users/sign_out
    def destroy
      respond_to do |format|
        format.html { super }
        format.json do
          begin
            token = Token.find_by_token(params[:token])
            token.destroy
            render json: { status: STATUS_SUCCESS }
          rescue
            render json: { status: STATUS_FAIL }
          end
        end
      end
    end

    protected

    # allow other params
    def configure_sign_in_params
      devise_parameter_sanitizer.for(:sign_in) << :login << :token
    end

  end
end
