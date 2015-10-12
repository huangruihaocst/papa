class Course < ActiveRecord::Base
  validates :name, presence: true

  belongs_to :semester

  has_many :lessons

  has_many :participations
  has_many :students, through: :participations, source: :user

  has_many :teaching_courses
  has_many :teachers, through: :teaching_courses, source: :user

  has_many :messages

  has_many :course_files
  has_many :attached_files, through: :course_files, source: :file_resource
end
