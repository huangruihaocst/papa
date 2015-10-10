class CoursesController < ApplicationController

  before_action :set_course, only: [:show]
  before_action only: [:index] do
    if params[:student_id]
      @id_key = :student_id
      @role = ROLE_STUDENT
      check_token(params[:token], params[@id_key])
    elsif params[:assistant_id]
      @id_key = :assistant_id
      @role = ROLE_ASSISTANT
      check_token(params[:token], params[@id_key])
    end
  end

  # GET /courses
  # GET /courses.json
  def index
    if @id_key
      @courses = Course.none
      User.find(params[@id_key]).participations.each do |participation|
        @courses <<= participation.course if @role == participation.role
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
  # POST /courses.json
  def create
    @course = Course.create(params.require(:course).permit(:name))
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
