class LessonStatus < ActiveRecord::Base
  belongs_to :user
  belongs_to :lesson
  belongs_to :creator, class_name: 'User'

  # string score, timestamps
end
