class Lesson < ActiveRecord::Base
  validates :name,        presence: true
  # description, nullable
  validates :start_time,  presence: true
  validates :end_time,    presence: true
  validates :location,    presence: true

  belongs_to :course
  has_many :lesson_files, dependent: :destroy
  has_many :attached_files, through: :lesson_files, source: :file_resource
  has_many :lesson_comments, dependent: :destroy
  has_many :student_comments, dependent: :destroy
  has_many :student_files, dependent: :destroy
  has_many :lesson_statuses, dependent: :destroy
  has_many :student_attendences, dependent: :destroy
  has_many :signed_in_students, through: :student_attendences, source: :user

  def latitude
    0
  end
  def longitude
    0
  end
  def radius
    1000
  end

end
