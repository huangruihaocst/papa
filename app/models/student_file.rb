class StudentFile < ActiveRecord::Base
  belongs_to :student, class_name: 'User'
  belongs_to :lesson
  belongs_to :file_resource
end