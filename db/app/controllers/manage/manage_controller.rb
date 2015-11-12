module  Manage
class ManageController < ApplicationController
  layout "application"
  before_action :authenticate_user!
  before_action :prepare_data
  def index
  end
  def main_page
    if current_user.is_admin
      render :template => "manage/manage/admin"
    end
  end
  def course_score
    @id= params[:id]
  end
  def class_score
    @id= params[:id]
  end
  def course_info
    @id= params[:id]
  end
  def class_info
    @id= params[:id]
  end
  def show_photos
    @id= params[:id]
  end
  def student_lesson_info
    begin
      @student = User.find(params[:id])
      @lesson = Lesson.find(params[:lessonId])
      @avator = FileResource.find(@student.avator_id)
    rescue
      puts "error:#{$!} at:#{$@}"
      @avator = Object.new
      def @avator.path
        "/photos/noAvatar.jpg"
      end
    end
  end
  def course_students
    @course_id = params[:id]
    @course_name = Course.find(@course_id).name;
  end
  def message
  end

  private
  def prepare_data
    @message_number =
        current_user.user_messages
                         .where(status: MESSAGE_STATUS_UNREAD)
                         .where(receiver_deleted: false).count
  end
end
end