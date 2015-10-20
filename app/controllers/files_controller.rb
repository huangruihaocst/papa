class FilesController < ApplicationController
  FILE_MAX_SIZE=10000000
  # GET /students/1/lessons/1/files.json
  # GET /assistants/1/lessons/1/files.json
  # GET /lessons/1/files.json
  # GET /courses/1/files.json
  def index
    case
      when params[:student_id] && params[:lesson_id]
        check_token(params[:student_id], params[:token])
        @files = StudentFile.where(student_id: params[:student_id]).where(lesson_id: params[:lesson_id])
      when params[:assistant_id] && params[:lesson_id]
        check_token(params[:assistant_id], params[:token])
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

  # POST /files.json
  def create
    user = check_login

    p = params[:file]
    unless p && p.is_a?(ActionController::Parameters) && p[:file] && p[:type]
      json_failed_invalid_fields([:file, :type])
      return
    end

    temp_file = p[:file]
    if temp_file.size > FILE_MAX_SIZE
      json_failed(REASON_FILE_TOO_BIG)
      return
    end

    rel_loc = File.join('uploads', temp_file.original_filename)
    loc = Rails.root.join('public', rel_loc)
    File.open(loc, 'wb') do |file|
      file.write(temp_file.read)
    end

    @file = FileResource.create(file_type: p[:type], name: temp_file.original_filename, path: File.join('', rel_loc), creator_id: user.id)
    if @file.valid?
        json_successful do |json|
          json['id'] = @file.id
        end
    else
      json_failed_invalid_fields(@file.errors.keys, file_type: :type, path: '', name: '')
    end
  end

  # DELETE /files/1.json
  def destroy
    user = check_login

    begin
      @file = FileResource.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_TOKEN_NOT_MATCH)
      return
    end

    unless @file.creator == user
      json_failed(REASON_TOKEN_NOT_MATCH)
      return
    end

    @file.destroy
    json_successful
  end

end
