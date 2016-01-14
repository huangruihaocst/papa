class TeachersController < ApplicationController

  # GET /courses/1/teachers.json
  def index
    begin
      @teachers = Course.find(params[:course_id]).teachers
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # POST /courses/1/teachers/1.json
  # POST /teachers.json
  def create
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
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # GET /teachers/1.json
  def show
    begin
      @teacher = User.find(params[:id])
      unless @teacher.is_teacher
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
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
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    elsif params[:id]
      check_admin
      begin
        @teacher = User.find(params[:id])
        json_failed(REASON_RESOURCE_NOT_FOUND) unless @teacher.is_teacher
        if @teacher.destroy
          json_successful
        else
          json_failed
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed(REASON_INVALID_OPERATION)
    end
  end

  # POST /lessons/1/start_sign_up.json
  def start_sign_up
    teacher = check_teacher
    lesson = Lesson.find(params[:lesson_id])
    if lesson.course.teachers.include?(teacher)
      json_successful
    else
      json_failed(REASON_PERMISSION_DENIED)
    end
  end

end
