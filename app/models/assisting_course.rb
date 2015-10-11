class AssistingCourse < ActiveRecord::Base
  # assistant
  belongs_to :user
  # the course you assist
  belongs_to :course

  validates_associated :user
  validates_associated :course
end
