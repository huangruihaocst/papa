class TeachersController < ApplicationController

  # GET /courses/1/teachers.json
  def index
    if params[:course_id]
      begin
        @teachers = Course.find(params[:course_id]).teachers
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed(REASON_INVALID_OPERATION)
    end
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
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

  # GET /teachers/1.json
  def show
    if params[:id]
      begin
        @teacher = User.find(params[:id])
        unless @teacher.is_teacher
          json_failed(REASON_RESOURCE_NOT_FOUND)
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    end
  end

  # PUT /teachers/1.json
  def update
    json_failed(REASON_NOT_IMPLEMENTED)
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

end
