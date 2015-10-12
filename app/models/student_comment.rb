class StudentComment < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  belongs_to :student, class_name: 'User'
  belongs_to :lesson
  # string score, text content, timestamps
end
