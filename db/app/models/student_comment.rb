class StudentComment < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  belongs_to :student, class_name: 'User'
  belongs_to :lesson
  # string score, text content, timestamps

  def self.from_lesson_and_student(lesson_id, student_id)
    StudentComment.where(lesson_id: lesson_id).where(student_id: student_id).last
  end
end
