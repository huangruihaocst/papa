class StudentsController < ApplicationController

  before_action only: [:show] do
    set_student
  end

  # GET /lessons/1/students.json (sign in the lesson)
  # GET /courses/1/students.json (student list of the course)
  def index
    if params[:course_id]
      begin
        course = Course.find(params[:course_id])
        user = check_login

        raise TokenException.new(REASON_PERMISSION_DENIED) unless course.teachers.include?(user) || course.assistants.include?(user)

        participations = course.participations.where(role: ROLE_STUDENT)
        @students = User.none
        participations.each do |p|
          @students <<= p.user
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    elsif params[:lesson_id]
      lesson = Lesson.find(params[:lesson_id])
      @students = lesson.signed_in_students
    else
      json_failed(REASON_INVALID_OPERATION)
    end
  end

  # GET /students/1.json
  def show
    check_login
    json_failed(REASON_RESOURCE_NOT_FOUND) unless @student
  end

  # POST /courses/1/students/1.json
  # POST /lessons/1/students/1.json
  def create
    if params[:course_id] && params[:id]
      course =  Course.find(params[:course_id])
      must_be_a_teacher_of(params[:token], course)
      if Participation.create(user_id: params[:id], course_id: params[:course_id], role: ROLE_STUDENT)
        json_successful
      else
        json_failed
      end
    elsif params[:lesson_id] && param[:id]
      sa = StudentAttendence.create(
          lesson_id: params[:lesson_id],
          user_id: params[:id],
          sign_in_method: params[:sign_in_method])
      if sa && sa.valid?
        json_successful
      else
        json_failed
      end
    else
      json_failed
    end
  end

  # POST /courses/1/students.json
  def create_many
    if params[:course_id]
      course = Course.find(params[:course_id])
      must_be_a_teacher_of(params[:token], course)
      json = params[:json]
      if json
        if json.is_a?(ActionController::Parameters)
          students = [ json ]
        else
          students = JSON.parse(json)
        end

        if students.is_a?(Array)
          invalid_students = []
          students.each do |student|
            error_message = ''
            error_message += ' no student number, ' unless student['student_number'].size > 0
            error_message += ' no name, ' unless student['name'].size > 0
            error_message += ' no email, ' unless student['email'].size > 0
            error_message += ' no phone number' unless student['phone'].size > 0
            raise RequestException.new(REASON_INVALID_FIELD + error_message) unless error_message.size == 0

            exist_user = User.find_by_student_number(student['student_number'])
            if exist_user
              exist_user.name = student['name']
              exist_user.email = student['email']
              exist_user.phone = student['phone']
              exist_user.department = student['department'] || ''
              exist_user.description = student['description'] || ''
              exist_user.class_name = student['class_name'] || ''
              exist_user.save
            else
              begin
                user = User.create(name: student['name'],
                                   email: student['email'],
                                   phone: student['phone'],
                                   password: student['student_number'], #初始密码是学号
                                   student_number: student['student_number'],
                                   department: student['department'] || '',
                                   description: student['description'] || '',
                                   class_name: student['class_name'] || '' )
              rescue
                raise RequestException.new(REASON_FORMAT_ERROR)
              end

              if user.valid?
                course.add_student(user)
                raise RequestException.new(REASON_INTERNAL_ERROR) unless course.save
              else
                if student['student_number']
                  invalid_students.push(student['student_number'])
                else
                  raise RequestException.new(REASON_INVALID_FIELD)
                end
              end
            end
          end
          if course.save
            json_successful(INVALID_FIELDS_NAME => invalid_students)
          else
            json_failed
          end
        else
          raise RequestException.new(REASON_FORMAT_ERROR)
        end
      end
    else
      json_failed
    end
  end

  # DELETE /courses/1/students/1.json
  def destroy
    if params[:course_id] && params[:id]
      begin
        course = Course.find(params[:course_id])
        must_be_a_teacher_of(params[:token], course)
        ps = Participation.where(user_id: params[:id]).where(course_id: params[:course_id])
        if ps.count > 0
          if ps.first.destroy
            json_successful
          else
            json_failed
          end
        else
          json_failed(REASON_RESOURCE_NOT_FOUND)
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed
    end
  end

  private
  def set_student
    begin
      student = User.find(params[:id])
      #@participation = student.participations.where(course_id: params[:course_id]).first
      #if @participation.role == ROLE_STUDENT
        @student = student
      #else
      #  @student = nil
      #end
    rescue ActiveRecord::RecordNotFound
      @student = nil
    end
  end

end
