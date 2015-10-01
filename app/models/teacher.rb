class Teacher < ActiveRecord::Base
  validates :name, presence: true
  validates :encrypted_password, presence: true

  has_many :teacher_courses
  has_many :courses, through: :teacher_courses
end