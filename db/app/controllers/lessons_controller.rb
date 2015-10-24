
class LessonsController < ApplicationController

  before_action :set_lesson, only: [:show, :destroy]

  # GET /courses/1/lessons.json
  def index
    if params[:course_id]
      begin
        @lessons = Course.find(params[:course_id]).lessons
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed(REASON_INVALID_OPERATION)
    end
  end

  # GET /lessons/1.json
  def show
    unless @lesson
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # POST /courses/1/lessons.json
  def create
    begin
      @course = Course.find(params[:course_id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
      return
    end

    must_be_a_teacher_of(params[:token], @course)

    @lesson = @course.lessons.create(
        params.require(:lesson).
        permit(:name, :description, :start_time, :end_time, :location).
            merge({ course_id: params[:course_id] }))

    if @lesson.valid?
      json_successful(id: @lesson.id)
    else
      json_failed(REASON_INVALID_FIELD)
    end
  end

  # PUT /lessons/1.json
  def update
    begin
      @lesson = Lesson.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
      return
    end

    must_be_a_teacher_of(params[:token], @lesson.course)
    if @lesson.update(params.require(:lesson).permit(:name, :description))
      json_successful
    else
      json_failed
    end
  end

  # DELETE /lessons/1.json
  def destroy
    if @lesson
      if @lesson.destroy
        json_successful
      else
        json_failed
      end
    else
      json_failed(REASON_RESOURCE_NOT_FOUND)
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