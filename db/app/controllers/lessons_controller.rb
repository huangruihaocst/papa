
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
    if @lesson
      if params[:full]
        @students = []
        @lesson.course.students.each do |student|
          comments = StudentComment.where(lesson_id: @lesson, student_id: student.id)
          comment_hash = {}
          if comments.count > 0
            comment = comments[0]
            comment_hash = {
                score:      comment.score,
                content:    comment.content,
                creator_id: comment.creator_id,
                creator_name: comment.creator.name,
                created_at: comment.created_at
            }
          end
          video_file_count, image_file_count = 0, 0
          student_files = @lesson.student_files
          student_files.each do |file|
            video_file_count += 1 if file.file_resource.type == FILE_TYPE_VIDEO
            image_file_count += 1 if file.file_resource.type == FILE_TYPE_IMAGE
          end

          student_info = {
              id:     student.id,
              name:   student.name,
              email:  student.email,
              phone:  student.phone,
              student_number:   student.student_number,
              comment:          comment_hash,
              video_count:      video_file_count,
              image_file_count: image_file_count
          }
          @students.push(student_info)
        end
      end
    else
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