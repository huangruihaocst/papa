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

  # GET /semesters/1/courses.json
  # GET /students/1/courses.json
  # GET /assistants/1/courses.json
  # GET /teachers/1/courses.json
  def index
    case
      when params[:semester_id]
        @courses = Semester.find(params[:semester_id]).courses
      when params[:student_id]
        check_token(params[:token], params[:student_id])
        @courses = Course.none
        User.find(params[@id_key]).participations.each do |participation|
          @courses <<= participation.course if ROLE_STUDENT == participation.role
        end
      when params[:assistant_id]
        check_token(params[:token], params[:student_id])
        @courses = Course.none
        User.find(params[@id_key]).participations.each do |participation|
          @courses <<= participation.course if ROLE_ASSISTANT == participation.role
        end
      when params[:teacher_id]
        check_token(params[:token], params[:teacher_id], true)
        @courses = User.find(params[:teacher_id]).teaching_courses
      else
        json_failed
    end

  end

  # GET /courses/1.json
  def show
    unless @course
      respond_to do |format|
        format.json { json_failed('not found') }
      end
    end
  end

  # POST /semesters/courses.json
  # POST /teachers/courses.json
  # POST /assistants/courses.json
  # POST /students/courses.json
  def create
    course_create_params = params.require(:course).permit(:name, :description)

    case
      when params[:semester_id]
        Semester.find(params[:semester_id]).courses.create(course_create_params)
      when params[:teacher_id]
        check_token(params[:token], params[:teacher_id], true)

      else
        json_failed
        return
    end

    respond_to do |format|
      if @course.valid?
        format.json { json_successful }
      else
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
