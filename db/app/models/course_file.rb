class CourseFile < ActiveRecord::Base
  belongs_to :course
  belongs_to :file_resource
end