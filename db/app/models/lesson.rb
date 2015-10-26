class Lesson < ActiveRecord::Base
  validates :name,        presence: true
  # description, nullable
  validates :start_time,  presence: true
  validates :end_time,    presence: true
  validates :location,    presence: true

  belongs_to :course
  has_many :lesson_files
  has_many :attached_files, through: :lesson_files, source: :file_resource
  has_many :lesson_comments
  has_many :student_comments
  has_many :student_files
  has_many :lesson_statuses
end
