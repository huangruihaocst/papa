class FileResource < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  has_one :student_file
  has_one :assistant_file
  has_one :lesson_file
  # string file_type, string name, string path, timestamps, creator

  validates :creator, presence: true
  validates :file_type, presence: true
  validates :name, presence: true
  validates :path, presence: true
end
