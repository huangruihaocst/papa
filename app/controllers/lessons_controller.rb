
class LessonsController < ApplicationController

  def api_index
    ids = []
    Lesson.all.each do |lesson|
      ids.push(lesson.id)
    end
    render json: {
               status: 'success',
               ids: ids
           }
  end

  def api_show
    begin
      lesson = Lesson.find(params[:id])
      render json: {
                 status: 'success',
                 result: {
                     id:          lesson.id,
                     name:        lesson.name,
                     start_time:  lesson.start_time,
                     end_time:    lesson.end_time,
                     position:    lesson.position
                 }
             }
    rescue ActiveRecord::RecordNotFound
      render json: {
                 status: 'not-found'
             }
    end
  end

  def api_add
    lesson =  Lesson.create(params.require(:lesson).permit(:name, :description, :start_time, :end_time, :position, :course_id))
    puts lesson.name
    if lesson.valid?
      render json: { status: 'success' }
    else
      render json: { status: 'fail' }
    end
  end

end