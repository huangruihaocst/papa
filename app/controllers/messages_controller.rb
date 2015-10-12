class MessagesController < ApplicationController
  # GET /students/1/messages.json
  def index
    @messages = Message.none

    courses = User.find(params[:student_id]).courses
    courses.each do |course|
      @messages += course.messages
    end
  end

  # POST /courses/1/messages.json
  def create
    if Message.create(
        params.require(:message).permit(:title, :type, :deadline, :content).merge(creator_id: current_user.id))
      json_successful
    else
      json_failed
    end
  end
end
