class Users::SessionsController < Devise::SessionsController
  # before_filter :configure_sign_in_params, only: [:create]
  #
  # #GET /resource/sign_in
  # def new
  #   super
  # end
  #
  # #POST /resource/sign_in
  def create
    respond_to do |format|
      format.html { super }
      format.json do
        self.resource = warden.authenticate!(auth_options)
        sign_in(resource_name, resource)
        render json: { status: STATUS_SUCCESS }
      end
    end

  end

  # #DELETE /resource/sign_out
  def destroy
    respond_to do |format|
      format.html { super }
      format.json do
        begin
          Devise.sign_out_all_scopes ? sign_out : sign_out(resource_name)
          render json: { status: STATUS_SUCCESS }
        rescue
          render json: { status: STATUS_FAIL }
        end
      end
    end
  end

  #
  # protected
  #
  # #If you have extra params to permit, append them to the sanitizer.
  # def configure_sign_in_params
  #   devise_parameter_sanitizer.for(:sign_in) << :attribute
  # end
end
