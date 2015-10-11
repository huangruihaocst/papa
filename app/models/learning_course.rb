class LearningCourse < ActiveRecord::Base
  # student
  belongs_to :user
  # the course you learn
  belongs_to :course

  validates_associated :user
  validates_associated :course
end
