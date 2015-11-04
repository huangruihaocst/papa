class Course < ActiveRecord::Base
  validates :name, presence: true

  belongs_to :semester

  has_many :lessons

  has_many :participations

  has_many :teaching_courses
  has_many :teachers, through: :teaching_courses, source: :user

  has_many :messages

  has_many :course_files
  has_many :attached_files, through: :course_files, source: :file_resource

  def students
    users = User.none
    participations.each do |p|
      users <<= p.user if p.role == ROLE_STUDENT
    end
    users
  end

  def add_student(student)
    Participation.create(user_id: student.id, course_id: id, role: ROLE_STUDENT)
  end

  def assistants
    users = User.none
    participations.each do |p|
      users <<= p.user if p.role == ROLE_ASSISTANT
    end
    users
  end

end
