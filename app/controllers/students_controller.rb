class StudentsController < ApplicationController

  before_action :set_student, only: [:show]

  def index
    course = Course.find(params[:course_id] || 1)
    participations = course.participations.where(role: ROLE_STUDENT)
    @students = User.none
    participations.each do |p|
      @students <<= p.user
    end
  end

  def show
    unless @student
      respond_to do |format|
        format.json { json_failed('not found') }
      end
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
