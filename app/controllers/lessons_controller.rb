
class LessonsController < ApplicationController

  before_action :set_lesson, only: [:show, :destroy]

  # GET /courses/1/lessons.json
  def index
    if params[:course_id]
      @lessons = Course.find(params[:course_id]).lessons
    else
      json_failed
    end
  end

  # GET /lessons/1.json
  def show
    unless @lesson
      respond_to do |format|
        format.html { html_failed('not found') }
        format.json { json_failed('not found') }
      end
    end
  end

  # POST /courses/1/lessons.json
  def create
    @lesson = Course.find(params[:course_id]).lessons.create(
        params.require(:lesson).
        permit(:name, :description, :start_time, :end_time, :location).
            merge({ course_id: params[:course_id] }))

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

  # DELETE /lessons/1.json
  def destroy
    if @lesson && @lesson.destroy
      json_successful
    else
      json_failed
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