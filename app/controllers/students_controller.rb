class StudentsController < ApplicationController

  before_action :set_student, only: [:show]

  def index
    course = Course.find(params[:course_id])
    participations = course.participations.where(role: ROLE_STUDENT)
    @students = User.none
    participations.each do |p|
      @students << p.student
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
      @participation = student.participations.where(course_id: params[:course_id])
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
