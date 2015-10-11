class LessonFile < ActiveRecord::Base
  belongs_to :lesson
  belongs_to :file_resource
end
