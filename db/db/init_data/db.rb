require './db/init_data/name_reader'
require './db/init_data/location_builder'
require './db/init_data/identity_builder'
require './db/init_data/course_builder'
require './db/init_data/message_builder'

# create semesters
SEMESTER_BEGIN = 2008
SEMESTER_COUNT = 10
SEMESTER_SPRING = '春季'
SEMESTER_SUMMER = '夏季'
SEMESTER_FALL = '秋季'

TEACHER_COUNT = 10
ASSISTANT_COUNT = 10
STUDENT_COUNT = 10
AS_COUNT = 10
LESSON_COMMENT_COUNT = 5
NOTIFICATION_COUNT = 5

name_reader = NameReader.new

semesters = []
SEMESTER_COUNT.times do |x|
  semesters.push(
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_FALL),
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_SPRING),
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_SUMMER)
  )
end
puts 'semesters created...'

# create courses
COURSE_COUNT = 10
start_time = Time.new(2000, 1, 1, 8, 0, 0)

courses = []
course_builder = CourseBuilder.new
COURSE_COUNT.times do |x|
  course = Course.create(name: course_builder.build,
                         description: '基础课程',
                         semester_id: semesters[0].id)
  i = 0
  CourseBuilder.build_lessons.each do |lesson_name|
    course.lessons.create(name: lesson_name,
                          start_time: start_time + (2+i).hours,
                          end_time: start_time + (3+i).hours,
                          location: LocationBuilder.build)
    i += 1
  end
  courses.push(course)

end
puts 'courses created...'

# create default admin and default avator
admin0 = User.create(name:'admin', phone:'0123', email:'a@b.c', password:'123', password_confirmation:'123',
                 student_number: '1230', description: '123', is_admin: true, avator_id: 1)
default_avator = FileResource.create(path: '/uploads/default_avator.jpg', name: 'default_avator.jpg', file_type: 'jpg', creator_id: admin0.id)
admin0.avator_id = default_avator.id
admin0.save
puts 'admin created...'

# create teachers

teachers = []
TEACHER_COUNT.times do |i|
  teacher = User.create(name: name_reader.read,
                        phone: "1#{i}",
                        email: "teacher#{i}@b.c",
                        password:'123', password_confirmation:'123',
                        student_number: "1#{i}",
                        description: '教师简介',
                        is_admin: false, is_teacher: true)
  teachers.push(teacher)
end

index_teacher = 0
courses.each do |course|
  TeachingCourse.create(user_id: teachers[index_teacher].id, course_id: course.id)

  index_teacher += 1
  index_teacher %= TEACHER_COUNT
end
puts 'teachers created...'

# create assistant for course 0

ASSISTANT_COUNT.times do |i|
  identity = IdentityBuilder.build_department_and_class
  user =  User.create(name: name_reader.read,
                      phone: "2#{i}",
                      email:"assistant#{i}@b.c",
                      password:'123', password_confirmation:'123',
                      student_number: "2#{i}",
                      class_name: identity[:class],
                      department: identity[:department],
                      description: 'xxx',
                      is_admin: false, is_teacher: false)
  Participation.create(user_id: user.id, course_id: courses[0].id, role: ROLE_ASSISTANT)
end
puts 'assistants created...'

# create students for course 0

students = []
STUDENT_COUNT.times do |x|
  identity = IdentityBuilder.build_department_and_class
  user = User.create(name: name_reader.read,
                     phone: "3#{x}",
                     email:"student#{x}@b.c",
                     password:'123', password_confirmation:'123',
                     student_number: "3#{x}",
                     class_name: identity[:class],
                     department: identity[:department],
                     description: '123',
                     is_admin: false, is_teacher: false)
  Participation.create(user_id: user.id, course_id: courses[0].id, role: ROLE_STUDENT)
  students.push(user)
end
puts 'students created...'

# create student and assistant
as = []
AS_COUNT.times do |x|
  identity = IdentityBuilder.build_department_and_class
  user = User.create(name: name_reader.read,
                     phone: "4#{x}",
                     email:"as#{x}@b.c",
                     password:'123', password_confirmation:'123',
                     student_number: "4#{x}",
                     class_name: identity[:class],
                     department: identity[:department],
                     description: '123',
                     is_admin: false, is_teacher: false)
  courses.each do |course|
    Participation.create(user_id: user.id, course_id: course.id, role: course.id % 2 == 0 ? ROLE_STUDENT : ROLE_ASSISTANT)
  end
  as.push(user)
end
puts 'assistant and student created...'

# create files
f1 = FileResource.create(name: '1.jpg', file_type: 'jpg', path: '/uploads/1.jpg', creator_id: admin0.id)
f2 = FileResource.create(name: '2.jpg', file_type: 'jpg', path: '/uploads/2.jpg', creator_id: admin0.id)
puts 'files created...'

## create lesson comments
courses[0].lessons.each do |lesson|
  LESSON_COMMENT_COUNT.times do
    lesson.course.students.each do |student|
      teacher = teachers[Random.rand(teachers.size)]
      lesson.student_comments.create(creator_id: teacher.id,
                                     student_id: student.id,
                                     score: Random.rand(100),
                                     content: '实验做的不错')
    end
  end
  lesson.lesson_comments.create(score: Random.rand(10),
                                content: '这个课程真好呀!',
                                creator_id: teachers[Random.rand(teachers.size)].id)
end
puts 'student comments created...'

# create messages
# notification
courses.each do |course|
  NOTIFICATION_COUNT.times do |x|
    course.messages.create(
        title: MessageBuilder.build_notification,
        content: '同学们千万不要忘了!',
        deadline: Time.now,
        creator: teachers[0],
        message_type: :notification)
  end
end
puts 'notifications created...'

# homework
HOMEWORK_COUNT = 5
courses.each do |course|
  HOMEWORK_COUNT.times do |x|
    course.messages.create(
        title: MessageBuilder.build_homework,
        content: '请同学们尽快完成',
        deadline: Time.now,
        creator: teachers[0],
        message_type: :homework)
  end
end
puts 'homework created...'

# user messages
USER_MESSAGES_COUNT = 5
teachers.each do |teacher|
  students.each do |student|
    UserMessage.create(sender_id: student.id,
                       receiver_id: teacher.id,
                       title: '老师我请个假行吗',
                       content: '生病了...',
                       status: MESSAGE_STATUS_UNREAD)
    UserMessage.create(sender_id: teacher.id,
                       receiver_id: student.id,
                       title: %w"行 不行"[Random.rand(2)],
                       content: '内容描述',
                       status: MESSAGE_STATUS_UNREAD)
  end
end
puts 'user messages created...'

puts '--- seed created ---'