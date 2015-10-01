class TeacherCourse < ActiveRecord::Base
  self.table_name = 'teachers_courses'

  belongs_to  :teacher
  belongs_to  :course
end