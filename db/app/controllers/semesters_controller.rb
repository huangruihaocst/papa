class SemestersController < ApplicationController

  def index
    @semesters = Semester.all.reverse_order
  end

  def default
    @semester = Semester.all.order(:name).last
  end

  def create
    check_admin

    semester = Semester.create(params.require(:semester).permit(:name))
    if semester
        json_successful(id: semester.id)
    else
      json_failed
    end
  end

  def update
    check_admin

    begin
      @semester = Semester.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
      return
    end
    
    if @semester.update(params.require(:semester).permit(:name))
      json_successful
    else
      json_failed
    end
  end

  def destroy
    check_admin

    begin
      @semester = Semester.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      json_failed(REASON_RESOURCE_NOT_FOUND)
      return
    end

    @semester.destroy
    json_successful
  end

end
