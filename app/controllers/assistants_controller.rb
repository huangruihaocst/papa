class AssistantsController < ApplicationController

  before_action only: [:show] do
    set_assistant

    raise TokenException.new(REASON_TOKEN_INVALID) unless @assistant
    check_token(params[:token], @assistant.id)
  end

  def index
    course = Course.find(params[:course_id] || 1)
    participations = course.participations.where(role: ROLE_ASSISTANT)
    @assistants = User.none
    participations.each do |p|
      @assistants <<= p.user
    end
  end

  def show
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
