class LessonFile < ActiveRecord::Base
  belongs_to :lesson
  belongs_to :file_resource
  belongs_to :creator, class_name: 'User'
end
