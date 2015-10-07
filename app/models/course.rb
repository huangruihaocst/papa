class Course < ActiveRecord::Base
  validates :name, presence: true

  has_many :lessons
  has_many :participations

  has_many :teacher_courses
  has_many :teachers, through: :teacher_courses
end
