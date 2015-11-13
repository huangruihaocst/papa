class LessonCommentsController < ApplicationController

  # GET /lessons/1/comments.json
  # GET /courses/1/comments.json
  def index
    if params[:lesson_id]
      @lesson_comments = Lesson.find(params[:lesson_id]).lesson_comments
      puts @lesson_comment.inspect
    else
      json_failed(REASON_NOT_IMPLEMENTED)
    end
  end

  # POST /lessons/1/comments.json
  def create
    user = check_login
    lesson = Lesson.find(params[:lesson_id])
    raise RequestException.new(REASON_PERMISSION_DENIED) unless lesson.course.students.include?(user)

    comment = lesson.lesson_comments.create(
        params.require(:lesson_comment).permit(:content, :score)
                                        .merge(creator_id: user.id)
    )
    if comment && comment.valid?
      json_successful(id: comment.id)
    else
      json_failed
    end
  end

  # GET /lessons/1/students/1/comment.json
  def default
    lesson = Lesson.find(params[:lesson_id])
    student = User.find(params[:student_id])
    raise RequestException.new(REASON_PERMISSION_DENIED) unless lesson.course.students.include?(student)

    comments = lesson.lesson_comments.where(creator_id: student.id)
    raise RequestException.new(REASON_RESOURCE_NOT_FOUND) unless comments.count > 0

    @lesson_comment = comments.last
    puts @lesson_comment.inspect
  end

end
