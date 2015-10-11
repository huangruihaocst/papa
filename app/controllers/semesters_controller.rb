class SemestersController < ApplicationController

  def index
    @semesters = Semester.all
  end

  def default
    Semester.all.order(:name).last
  end

  def create
    Semester.create()
  end

  def update

  end

  def delete

  end

end
