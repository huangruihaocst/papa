class AndroidAppsController < ApplicationController

  # GET /android_apps/current_version.json
  def current_version
    @android_app = AndroidApp.order(:created_at).last
    render json: { status: STATUS_SUCCESS, android_app: { version: @android_app.version, file_resource_id: @android_app.file_resource.id } }
  end

  # POST /android_apps/current_version.json
  def create
    check_admin

    if AndroidApp.create(params.require(:android_app).permit(:version, :file_resource_id))
      json_successful
    else
      json_failed(REASON_INVALID_FIELD)
    end
  end

end
