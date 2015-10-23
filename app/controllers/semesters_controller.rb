class SemestersController < ApplicationController

  def index
    @semesters = Semester.all
  end

  def default
    if Semester.count > 0
      @semester = Semester.all.order(:name).last
    else
      json_failed(REASON_INVALID_FIELD)
    end
  end

  def create
    check_admin

    semester = Semester.create(params.require(:semester).permit(:name))
    if semester
        json_successful
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

    if @semester.destroy
      json_successful
    else
      json_failed
    end

  end

end
