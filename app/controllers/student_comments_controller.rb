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
      if StudentComment.create(
          params.require(:student_comment).permit(:content, :score).merge({lesson_id: params[:lesson_id], student_id: params[:student_id], creator_id: current_user.id}))
        json_successful
      else
        json_failed
      end
    end
  end

  def default
    if params[:lesson_id] && params[:student_id]
      @student_comment = StudentComment.where(lesson_id: params[:lesson_id]).where(student_id: params[:student_id]).last
    else
      json_failed
    end
  end

end
