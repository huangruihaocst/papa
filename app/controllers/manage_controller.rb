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
      uId=@current_user.id;
      c=Course.where("name = ?",params[:name]).first;
      if(not c)
        c=Course.create(name: params[:name],description : params[:description])
      end

    end
  end
end
