class CoursesController < ApplicationController

  before_action :set_course, only: [:show]

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
        @courses = User.find(params[:teacher_id]).real_teaching_courses
      when params[:student_id]
        check_token(params[:token], params[:student_id])
        @courses = Course.none
        User.find(params[:student_id]).participations.each do |participation|
          @courses <<= participation.course if ROLE_STUDENT == participation.role
        end
      when params[:assistant_id]
        check_token(params[:token], params[:assistant_id])
        @courses = Course.none
        User.find(params[:assistant_id]).participations.each do |participation|
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

  # POST /teachers/1/courses.json
  # POST /assistants/1/courses/1.json
  # POST /students/1/courses/1.json
  def create
    course_create_params = params.require(:course).permit(:name, :description, :semester_id)
    begin
      case
        when params[:teacher_id]
          check_token(params[:token], params[:teacher_id], true)
          teacher = User.find(params[:teacher_id])
          course = Course.create(course_create_params)
          if teacher.teaching_courses.create(course_id: course.id)
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

  # PUT /courses/1.json
  def update
    if Course.find(params[:id]).update(params.require(:course).permit(:name, :description, :semester_id))
      json_successful
    else
      json_failed
    end
  end

  def destroy
    if Course.find(params[:id]).destroy
      json_successful
    else
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
