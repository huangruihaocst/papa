class MessagesController < ApplicationController
  # GET /students/1/messages.json
  def index
    begin
      student = User.find(params[:student_id])
      if student && check_token(student.id, params[:token])
        @messages = Message.none
        courses = student.courses
        courses.each do |course|
          @messages += course.messages
        end
      else
        json_failed(REASON_PERMISSION_DENIED)
      end
    rescue ActiveRecord::RecordNotFound
      json_failed_invalid_fields([:student_id])
    end

  end

  # POST /courses/1/messages.json
  def create
    admin = check_admin

    @message = Message.create(
        params.require(
            :message
        ).permit(
            :title, :deadline, :content
        ).merge(
            creator_id:   admin.id,
            message_type: params[:message][:type],
            course_id:    params[:course_id]
        )
    )

    if @message && @message.valid?
      json_successful
    else
      json_failed
    end
  end
end
