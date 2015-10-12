class CourseFile < ActiveRecord::Base
  belongs_to :course
  belongs_to :file_resource
  belongs_to :creator, class_name: 'User'
end