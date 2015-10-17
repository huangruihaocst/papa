class TeachersController < ApplicationController

  # GET /courses/1/teachers.json
  def index
    @teachers = Course.find(params[:course_id]).teachers
  end

  # POST /courses/1/teachers/1.json
  # POST /teachers.json
  def create
    if params[:id]
      begin
        check_admin
        course = Course.find(params[:course_id])
        teacher = User.find(params[:id])
        course.teachers << teacher
        if course.save
          json_successful
        else
          json_failed
        end
      rescue ActiveRecord::RecordNotFound
        json_failed_invalid_fields([:course_id, :id])
      end
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

  # DELETE /courses/1/teachers/1.json
  # DELETE /teachers/1.json
  def destroy
    if params[:course_id]
      begin
        check_admin
        course = Course.find(params[:course_id])
        teacher = User.find(params[:id])
        if teacher.teaching_courses.where(course_id: course.id).first.destroy
          json_successful
        else
          json_failed
        end
      rescue ActiveRecord::RecordNotFound
        json_failed_invalid_fields([:course_id, :id])
      end
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

end
