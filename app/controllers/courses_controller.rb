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
      when params[:teacher_id]
        check_token(params[:token], params[:teacher_id], true)
        @courses = User.find(params[:teacher_id]).teaching_courses
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

  # POST /semesters/1/courses.json
  # POST /teachers/1/courses.json
  # POST /assistants/1/courses/1.json
  # POST /students/1/courses/1.json
  def create
    course_create_params = params.require(:course).permit(:name, :description, :semester_id)

    begin
      case
        when params[:teacher_id]
          check_token(params[:token], params[:teacher_id], true)
          if User.find(params[:teacher_id]).courses.create(course_create_params)
            json_successful
          else
            json_failed
          end
        when params[:assistant_id] && params[:course_id]
          check_token(params[:token], params[:assistant_id])
          assistant = User.find(params[:assistant_id])
          course = Course.find(params[:course_id])
          assistant.courses << course
          if assistant.save
            json_successful
          else
            json_failed
          end
        when params[:student_id] && params[:course_id]
          check_token(params[:token], params[:student_id])
          student = User.find(params[:student_id])
          course = Course.find(params[:course_id])
          student.courses << course
          if student.save
            json_successful
          else
            json_failed
          end
        else
          json_failed
      end
    rescue
      json_failed
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
