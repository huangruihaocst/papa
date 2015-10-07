class ManageController < ApplicationController
  layout "main"
  #before_action :authenticate_user!
  def index
  end
  def MainPage
  end
  def CourseScore
  end
  def ClassScore
  end
  def CourseInfo
  end
  def StudentInfo
  end
  def ClassInfo
  end
  def ShowPhotos
  end
  def ShowVideos
  end
  def GetUserCoursesById
    reqId=params[:id]
    render :json => User.find(reqId).courses.to_json
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
