class CoursesController < ApplicationController

  # returns a json that:
  #   { "status": "success"/"failed", "ids": [the IDs of all courses] }
  def api_index
    ids = []
    Course.all.each do |course|
      ids.push(course.id)
    end
    render json: {
      status: 'success',
      ids:    ids
    }
  end

  def api_show
    begin
      course = Course.find(params[:id])
      render json: {
                 status: 'success',
                 result: {
                     id:          course.id,
                     name:        course.name,
                     exp_count:   course.lessons.count
                 }
             }
    rescue ActiveRecord::RecordNotFound
      render json: {
                 status: 'not-found'
             }
    end
  end

  def api_add
    course =  Course.create(params.require(:course).permit(:name))
    if course.valid?
      render json: { status: 'success' }
    else
      render json: { status: 'fail' }
    end
  end
end
