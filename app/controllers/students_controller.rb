class StudentsController < ApplicationController

  before_action only: [:show] do
    set_student

    raise TokenException.new(REASON_TOKEN_INVALID) unless @student
    check_token(params[:token], @student.id)
  end

  # GET /lessons/1/students.json (sign in the lesson)
  # GET /courses/1/students.json (student list of the course)
  def index
    if params[:course_id]
      course = Course.find(params[:course_id])
      participations = course.participations.where(role: ROLE_STUDENT)
      @students = User.none
      participations.each do |p|
        @students <<= p.user
      end
    else

    end

  end

  # GET /students/1.json
  def show
    @student = User.find(params[:id])
  end

  # POST /courses/1/students/1.json
  def create
    if params[:course_id] && params[:id]
      if Course.find(params[:course_id]).students << User.find(params[:id])
        json_successful
      else
        json_failed
      end
    else
      json_failed
    end
  end

  # DELETE /courses/1/students/1.json
  def destroy
    if params[:course_id] && params[:id]
      if Participation.where(user_id: params[:id]).where(course_id: params[:course_id]).first.destroy
        json_successful
      else
        json_failed
      end
    else
      json_failed
    end
  end

  private
  def set_student
    begin
      student = User.find(params[:id])
      ### just for debug, default course_id is 1
      @participation = student.participations.where(course_id: params[:course_id] || 1).first
      if @participation.role == ROLE_STUDENT
        @student = student
      else
        @student = nil
      end
    rescue ActiveRecord::RecordNotFound
      @student = nil
    end
  end

end
