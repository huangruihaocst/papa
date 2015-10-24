class StudentsController < ApplicationController

  before_action only: [:show] do
    set_student
  end

  # GET /lessons/1/students.json (sign in the lesson)
  # GET /courses/1/students.json (student list of the course)
  def index
    if params[:course_id]
      begin
        course = Course.find(params[:course_id])
        must_be_a_teacher_of(params[:token], course)

        participations = course.participations.where(role: ROLE_STUDENT)
        @students = User.none
        participations.each do |p|
          @students <<= p.user
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    elsif params[:lesson_id]
      json_failed(REASON_NOT_IMPLEMENTED)
    else
      json_failed(REASON_INVALID_OPERATION)
    end
  end

  # GET /students/1.json
  def show
    check_login
    json_failed(REASON_RESOURCE_NOT_FOUND) unless @student
  end

  # POST /courses/1/students/1.json
  # POST /lessons/1/students/1.json
  def create
    if params[:course_id] && params[:id]
      course =  Course.find(params[:course_id])
      must_be_a_teacher_of(params[:token], course)
      if Participation.create(user_id: params[:id], course_id: params[:course_id], role: ROLE_STUDENT)
        json_successful
      else
        json_failed
      end
    elsif params[:lesson_id] && param[:id]
      json_failed(REASON_NOT_IMPLEMENTED)
    else
      json_failed
    end
  end

  # DELETE /courses/1/students/1.json
  def destroy
    if params[:course_id] && params[:id]
      begin
        course = Course.find(params[:course_id])
        must_be_a_teacher_of(params[:token], course)
        ps = Participation.where(user_id: params[:id]).where(course_id: params[:course_id])
        if ps.count > 0
          if ps.first.destroy
            json_successful
          else
            json_failed
          end
        else
          json_failed(REASON_RESOURCE_NOT_FOUND)
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed
    end
  end

  private
  def set_student
    begin
      student = User.find(params[:id])
      #@participation = student.participations.where(course_id: params[:course_id]).first
      #if @participation.role == ROLE_STUDENT
        @student = student
      #else
      #  @student = nil
      #end
    rescue ActiveRecord::RecordNotFound
      @student = nil
    end
  end

end
