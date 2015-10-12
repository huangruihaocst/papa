class SemestersController < ApplicationController

  def index
    @semesters = Semester.all
  end

  def default
    @semester = Semester.all.order(:name).last
  end

  def create
    respond_to do |format|
      if Semester.create(params.require(:semester).permit(:name))
        format.json { json_successful }
      else
        format.json { json_failed }
      end
    end
  end

  def update
    respond_to do |format|
      if Semester.find(params[:id]).update(params.require(:semester).permit(:name))
        format.json { json_successful }
      else
        format.json { json_failed }
      end
    end
  end

  def destroy
    respond_to do |format|
      if Semester.find(params[:id]).destroy
        format.json { json_successful }
      else
        format.json { json_failed }
      end
    end
  end

end
