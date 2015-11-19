class AttendanceController < ApplicationController

  # POST /lessons/1/attendance.json
  def create
    user = check_login
    lesson = Lesson.find(params[:lesson_id])
    if StudentAttendence.create(user_id: user.id, lesson_id: lesson.id, sign_up_method: params[:method]).valid?
      json_successful
    else
      json_failed
    end
  end

  # DELETE /lessons/1/attendance.json
  def destroy
    user = check_login
    lesson = Lesson.find(params[:lesson_id])
    attendance = StudentAttendence.where(lesson_id: lesson.id, user_id: user.id)
    if attendance.count > 0
      attendance.each do |att|
        unless att.destroy
          json_failed
          return
        end
      end
      json_successful
    else
      json_failed
    end
  end

end