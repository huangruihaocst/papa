class Course < ActiveRecord::Base
  validates :name, presence: true

  belongs_to :semester

  has_many :lessons

  has_many :participations
  has_many :students, through: :participations, foreign_key: :user, class_name: 'User'

  has_many :teaching_courses
  has_many :teachers, through: :teaching_courses, foreign_key: :user, class_name: 'User'

  has_many :messages

end
