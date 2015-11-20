class Lesson < ActiveRecord::Base
  validates :name,        presence: true
  # description, nullable
  validates :start_time,  presence: true
  validates :end_time,    presence: true
  validates :location,    presence: true

  belongs_to :course, dependent: :destroy
  has_many :lesson_files
  has_many :attached_files, through: :lesson_files, source: :file_resource
  has_many :lesson_comments
  has_many :student_comments
  has_many :student_files
  has_many :lesson_statuses
  has_many :student_attendences
  has_many :signed_in_students, through: :student_attendences, source: :user

  def latitude
    40.004564
  end
  def longitude
    116.327893
  end
  def radius
    1000
  end

end
