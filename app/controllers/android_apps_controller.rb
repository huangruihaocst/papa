class AndroidAppsController < ApplicationController

  # GET /android_apps/current_version.json
  def current_version
    @android_app = AndroidApp.order(:created_at).last
    render json: { status: STATUS_SUCCESS, android_app: { version: @android_app.version, file_resource_id: @android_app.file_resource.id } }
  end

  # POST /android_apps/current_version.json
  def create
    if AndroidApp.create(params.require(:android_app).permit(:version, :file_resource_id))
      json_successful
    else
      json_failed
    end
  end

end
