class AttendanceController < ApplicationController

  # POST /lessons/1/students/1.json
  def create
    user = check_login
    lesson = Lesson.find(params[:lesson_id])
    if StudentAttendence.create(user_id: user.id, lesson_id: lesson.id, sign_up_method: params[:method]).valid?
      json_successful
    else
      json_failed
    end
  end
end