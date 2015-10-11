class TeachingCourse < ActiveRecord::Base
  # teacher
  belongs_to :user
  # the course you teach
  belongs_to :course

  validates_associated :user
  validates_associated :course
end
