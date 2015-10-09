class StudentsController < ApplicationController

  before_action only: [:show] do
    set_student

    raise TokenException.new(REASON_TOKEN_INVALID) unless @student
    check_token(params[:token], @student.id)
  end

  def index
    course = Course.find(params[:course_id] || 1)
    participations = course.participations.where(role: ROLE_STUDENT)
    @students = User.none
    participations.each do |p|
      @students <<= p.user
    end
  end

  def show
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
