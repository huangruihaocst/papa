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

  end

  def delete

  end

end
