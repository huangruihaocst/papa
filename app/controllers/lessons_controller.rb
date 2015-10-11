
class LessonsController < ApplicationController

  before_action :set_lesson, only: [:show]

  # GET /lessons
  # GET /lessons.json
  def index
    @lessons = Lesson.all
  end

  # GET /lessons/1
  # GET /lessons/1.json
  def show
    unless @lesson
      respond_to do |format|
        format.html { html_failed('not found') }
        format.json { json_failed('not found') }
      end
    end
  end

  # POST /lessons
  def create
    @lesson =  Lesson.create(
        params.require(:lesson).
            permit(:name, :description, :start_time, :end_time, :location).merge({ course_id: params[:course_id] }))

    respond_to do |format|
      if @lesson.valid?
        format.html { redirect_to @lesson }
        format.json { json_successful }
      else
        format.html { html_failed }
        format.json { json_failed }
      end
    end
  end

  private
  def set_lesson
    begin
      @lesson = Lesson.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      @lesson = nil
    end
  end

end