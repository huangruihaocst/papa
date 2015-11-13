class CoursesController < ApplicationController

  before_action :set_course, only: [:show, :update, :destroy]

  # GET /semesters/1/courses.json
  # GET /students/1/courses.json
  # GET /assistants/1/courses.json
  # GET /teachers/1/courses.json
  def index
    begin
      case
        when params[:semester_id]
          if params[:my_courses]
            user = check_login
            @courses = user.courses.where(semester_id: params[:semester_id])
          else
            @courses = Semester.find(params[:semester_id]).courses
          end
        when params[:teacher_id]
          check_token(params[:teacher_id], params[:token], true)
          @courses = User.find(params[:teacher_id]).real_teaching_courses
        when params[:student_id]
          check_token(params[:student_id])
          @courses = Course.none
          User.find(params[:student_id]).participations.each do |participation|
            @courses <<= participation.course if ROLE_STUDENT == participation.role
          end
        when params[:assistant_id]
          check_token(params[:assistant_id])
          @courses = Course.none
          User.find(params[:assistant_id]).participations.each do |participation|
            @courses <<= participation.course if ROLE_ASSISTANT == participation.role
          end
        else
          json_failed
      end
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # GET /courses/1.json
  def show
    if @course
      if params[:full]
        @students = []
        students = @course.students
        students.each do |student|
          student_info = {
              id: student.id,
              name: student.name,
              email: student.email,
              phone: student.phone,
              student_number:   student.student_number,
              department:       student.department,
              class_name:       student.class_name,
              avator_id:        student.avator_id,
              description:      student.description
          }
          lesson_info_of_student = []
          @course.lessons.each do |lesson|
            comments = StudentComment.where(lesson_id: lesson.id).where(student_id: student.id)
            if comments.count > 0
              comment = comments[0]
              lesson_info_of_student.push({
                      id:           comment.id,
                      content:      comment.content,
                      score:        comment.score,
                      creator_id:   comment.creator_id,
                      creator_name: comment.creator.name
                  })
            end
          end
          @students.push({ student_info: student_info, lessons: lesson_info_of_student })
        end
      end
    else
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # POST /teachers/1/courses.json
  # POST /assistants/1/courses/1.json
  # POST /students/1/courses/1.json
  def create
    begin
      case
        when params[:teacher_id]
          course_create_params = params.require(:course).permit(:name, :description, :semester_id)
          check_token(params[:teacher_id], params[:token], true)
          teacher = User.find(params[:teacher_id])
          course = Course.create(course_create_params)
          if teacher.teaching_courses.create(course_id: course.id)
            json_successful(id: course.id)
          else
            json_failed
          end
        when params[:assistant_id] && params[:id]
          check_token(params[:assistant_id])
          if Participation.create(user_id: params[:assistant_id],
              course_id: params[:id], role: ROLE_ASSISTANT).valid?
            json_successful
          else
            json_failed
          end
        when params[:student_id] && params[:id]
          check_token(params[:student_id])
          if Participation.create(user_id: params[:student_id],
               course_id: params[:id], role: ROLE_STUDENT).valid?
            json_successful
          else
            json_failed
          end
        else
          json_failed(REASON_INVALID_OPERATION)
      end
    rescue
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # PUT /courses/1.json
  def update
    must_be_a_teacher_of(params[:token], @course)
    if @course.update(params.require(:course).permit(:name, :description, :semester_id))
      json_successful
    else
      json_failed
    end
  end

  # DELETE /courses/1.json
  # DELETE /students/1/courses/1.json
  # DELETE /assistants/1/courses/1.json
  def destroy
    must_be_a_teacher_of(params[:token], @course)
    case
      when params[:student_id]
        p = Participation.where(user_id: params[:student_id], course_id: @course.id, role: ROLE_STUDENT)
        if p.count > 0 && p.first.destroy
          json_successful
        else
          json_failed(REASON_RESOURCE_NOT_FOUND)
        end
      when params[:assistant_id]
        p = Participation.where(user_id: params[:assistant_id], course_id: @course.id, role: ROLE_ASSISTANT)
        if p.first.destroy
          json_successful
        else
          json_failed(REASON_RESOURCE_NOT_FOUND)
        end
      when @course
        if @course.destroy
          json_successful
        else
          json_failed
        end
      else
        json_failed
    end
  end

  def set_course
    begin
      @course = Course.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      @course = nil
    end
  end
end
