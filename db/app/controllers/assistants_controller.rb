class AssistantsController < ApplicationController

  before_action only: [:show] do
    set_assistant
  end

  def index
    if params[:course_id]
      begin
        course = Course.find(params[:course_id])
        must_be_a_teacher_of(params[:token], course)

        participations = course.participations.where(role: ROLE_ASSISTANT)
        @assistants = User.none
        participations.each do |p|
          @assistants <<= p.user
        end
      rescue ActiveRecord::RecordNotFound
        json_failed(REASON_RESOURCE_NOT_FOUND)
      end
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

  def show
    unless @assistant
      json_failed(REASON_RESOURCE_NOT_FOUND)
    end
  end

  # relate an assistant to a course
  def create
    course =  Course.find(params[:course_id])
    must_be_a_teacher_of(params[:token], course)
    if Participation.create(user_id: params[:id], course_id: params[:course_id], role: ROLE_ASSISTANT)
      json_successful
    else
      json_failed
    end
  end

  # POST /courses/1/assistants.json
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
          invalid_assistants = []
          students.each do |assistant|
            raise RequestException.new(REASON_INVALID_FIELD) unless assistant['phone']
            exist_user = User.find_by_phone(assistant['phone'])
            if exist_user
              course.add_assistant(exist_user) if exist_user.courses.include?(course)
            else
              begin
                user = User.create(name: assistant['name'],
                                 email: assistant['email'],
                                 phone: assistant['phone'],
                                 password: assistant['email'],
                                 department: assistant['department'] || '',
                                 description: assistant['description'] || '',
                                 class_name: assistant['class_name'] || '')
              rescue
                json_failed
                return
              end
              if user.valid?
                course.add_assistant(user)
                raise RequestException.new(REASON_INTERNAL_ERROR) unless course.save
              else
                if assistant['phone']
                  invalid_assistants.push(assistant['phone'])
                else
                  raise RequestException.new(REASON_INVALID_FIELD)
                end
              end
            end
          end
          if course.save
            json_successful(INVALID_FIELDS_NAME => invalid_assistants)
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

  private

  def set_assistant
    begin
      assistant = User.find(params[:id])
      ### just for debug, default course_id is 1
      #@participation = assistant.participations.where(course_id: params[:course_id]).first
      #if @participation && @participation.role == ROLE_ASSISTANT
        @assistant = assistant
      #else
      #  @assistant = nil
      #end
    rescue ActiveRecord::RecordNotFound
      @assistant = nil
    end
  end

end
