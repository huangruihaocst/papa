class CourseFile < ActiveRecord::Base
  belongs_to :course, dependent: :destroy
  belongs_to :file_resource, dependent: :destroy
end