class ManageController < ApplicationController
  layout "main"
  #before_action :authenticate_user!
  def index
  end
  def main_page
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
    @student = User.find(params[:id])
    @lesson = Lesson.find(params[:lessonId])
  end
  def AddCourseToCurrentUser
    if(user_signed_in?)
      c=Course.where("name = ?",params[:name]).first;
      unless c
        c=Course.create(name: params[:name],description: params[:description])
      end
      unless User.find(@current_user.id).courses.where("course_id=?",c.id).first
        Participation.create(user_id: @current_user.id, course_id: c.id, role: params[:role]);
      end
      render :text => "succeed"
    else
      render :text => "fail"
    end
  end

end
