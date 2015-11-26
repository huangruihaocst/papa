
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
          comment = StudentComment.from_lesson_and_student(@lesson.id, student.id)
          comment_hash = {}
          if comment
            comment_hash = {
                score:      comment.score,
                content:    comment.content,
                creator_id: comment.creator_id,
                creator_name: comment.creator.name,
                created_at: comment.created_at
            }
          end
          video_file_count, image_file_count = 0, 0
          student_files = @lesson.student_files.where(student_id: student.id)
          student_files.each do |file|
            video_file_count += 1 if file.file_resource.file_type == FILE_TYPE_VIDEO
            image_file_count += 1 if file.file_resource.file_type == FILE_TYPE_IMAGE
          end

          student_attendances = @lesson.student_attendences.where(user_id: student.id)
          attendance = student_attendances.size > 0 ? student_attendances[0] : nil

          student_info = {
              id:     student.id,
              name:   student.name,
              email:  student.email,
              phone:  student.phone,
              student_number:   student.student_number,
              department:   student.department,
              avator_id:    student.avator_id,
              description:  student.description,
              class_name:   student.class_name,
              comment:          comment_hash,
              files_number: {
                  videos:      video_file_count,
                  images:   image_file_count
              },
              attendance: attendance ? attendance.created_at : ''
          }
          @students.push(student_info: student_info)
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

  # GET /teachers/1/lessons.json
  def from_teacher
    teacher = User.find(params[:teacher_id])
    @lessons = Lesson.none
    teacher.teaching_courses.each do |teaching_course|
      @lessons.merge(teaching_course.course.lessons)
    end
    @lessons
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