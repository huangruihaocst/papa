class CoursesController < ApplicationController

  before_action :set_course, only: [:show]

  # GET /courses
  # GET /courses.json
  def index
    if params[:student_id]
      @courses = Course.none
      User.find(params[:student_id]).participations.each do |participation|
        @courses <<= participation.course
      end
    else
      @courses = Course.all
    end

  end

  # GET /courses/1
  # GET /courses/1.json
  def show
    unless @course
      respond_to do |format|
        format.html { html_failed('not found') }
        format.json { json_failed('not found') }
      end
    end
  end

  # POST /courses
  def create
    @course =  Course.create(params.require(:course).permit(:name))
    respond_to do |format|
      if @course.valid?
        format.html { redirect_to @course }
        format.json { json_successful }
      else
        format.html { html_failed }
        format.json { json_failed('invalid params') }
      end
    end
  end

  private
  def set_course
    begin
      @course = Course.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      @course = nil
    end
  end



end
