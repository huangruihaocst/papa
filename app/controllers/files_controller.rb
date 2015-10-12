class FilesController < ApplicationController
  # GET /students/1/lessons/1/files.json
  # GET /assistants/1/lessons/1/files.json
  # GET /lessons/1/files.json
  # GET /courses/1/files.json
  def index
    case
      when params[:student_id] && params[:lesson_id]
        @files = StudentFile.where(student_id: params[:student_id]).where(lesson_id: params[:lesson_id])
      when params[:assistant_id] && params[:lesson_id]
        @files = AssistantFile.where(assistant_id: params[:assistant_id]).where(lesson_id: params[:lesson_id])
      when params[:lesson_id]
        @files = Lesson.find(params[:lesson_id]).attached_files
      when params[:course_id]
        @files = Course.find(params[:course_id]).attached_files
      else
        json_failed
    end
  end

  # GET /files/1.json
  def show
    @file = FileResource.find(params[:id])
  end

  def create
    json_failed(REASON_NOT_IMPLEMENTED)
  end

  def destroy
    json_failed(REASON_NOT_IMPLEMENTED)
  end

end
