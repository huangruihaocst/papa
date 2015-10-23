class LessonCommentsController < ApplicationController

  # GET /lessons/1/comments.json
  # GET /courses/1/comments.json
  def index
    if params[:lesson_id]
      @lesson_comments = Lesson.find(params[:lesson_id]).lesson_comments
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

  # POST /lessons/1/comments.json
  def create
    if check_current_user_student_of_lesson(params[:lesson_id])
      lesson = Lesson.find(params[:lesson_id])
      if lesson.lesson_comments.create(params.require(:lesson_comment).permit(:content, :score))
        json_successful
      else
        json_failed
      end
    end
  end

  protected
  def check_current_user_student_of_lesson(lesson)
    true
  end

end
