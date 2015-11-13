class StudentCommentsController < ApplicationController

  # GET /lessons/1/students/1/comments.json
  def index
    if params[:lesson_id] && params[:student_id]
      @student_comments = StudentComment.where(lesson_id: params[:lesson_id]).where(student_id: params[:student_id])
    else
      json_failed
    end
  end

  # POST /lessons/1/students/1/comments.json
  def create
    if params[:lesson_id] && params[:student_id]
      user = check_login
      student_comment = StudentComment.create(
          params.require(:student_comment).permit(:content, :score).merge({lesson_id: params[:lesson_id], student_id: params[:student_id], creator_id: user.id}))
      if student_comment && student_comment.valid?
        json_successful(id: student_comment.id)
      else
        json_failed
      end
    end
  end

  # GET /lessons/1/students/1/comment.json
  def default
    raise RequestException.new(REASON_INVALID_OPERATION) unless params[:lesson_id] && params[:student_id]
    @student_comment = StudentComment.where(lesson_id: params[:lesson_id]).where(student_id: params[:student_id]).last
    raise RequestException.new(REASON_RESOURCE_NOT_FOUND) unless @student_comment
  end

end
