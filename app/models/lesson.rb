class Lesson < ActiveRecord::Base
  validates :name,        presence: true
  # description, nullable
  validates :start_time,  presence: true
  validates :end_time,    presence: true
  validates :location,    presence: true

  belongs_to :course
  has_many :lesson_files
  has_many :attached_files, through: :lesson_files, class_name: 'FileResource', foreign_key: :file_resource_id
  has_many :lesson_remarks
  has_many :student_remarks
end
