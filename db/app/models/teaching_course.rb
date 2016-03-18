class TeachingCourse < ActiveRecord::Base
  # teacher
  belongs_to :user
  # the course you teach
  belongs_to :course

  validates :user, presence: true
  validates :course, presence: true
end
