class LessonRemark < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  belongs_to :lesson

  # int score, text content, timestamps
end
