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
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end

  end

  # POST /courses/1/messages.json
  def create
    user = check_login

    begin
      course = Course.find(params[:course_id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
      return
    end

    unless course.teachers.include?(user) || course.assistants.include?(user)
      json_failed(REASON_PERMISSION_DENIED)
      return
    end

    @message = Message.create(
        params.require(
            :message
        ).permit(
            :title, :deadline, :content
        ).merge(
            creator_id:   user.id,
            message_type: params[:message][:type],
            course_id:    params[:course_id]
        )
    )

    if @message && @message.valid?
      json_successful(id: @message.id)
    else
      json_failed(REASON_INVALID_FIELD)
    end
  end
end
