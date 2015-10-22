class AssistantsController < ApplicationController

  before_action only: [:show] do
    set_assistant

    raise TokenException.new(REASON_TOKEN_INVALID) unless @assistant
    check_token(params[:token], @assistant.id)
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

  def create
    json_failed(REASON_NOT_IMPLEMENTED)
  end

  private

  def set_assistant
    begin
      assistant = User.find(params[:id])
      ### just for debug, default course_id is 1
      @participation = assistant.participations.where(course_id: params[:course_id] || 1).first
      if @participation.role == ROLE_ASSISTANT
        @assistant = assistant
      else
        @assistant = nil
      end
    rescue ActiveRecord::RecordNotFound
      @assistant = nil
    end
  end

end
