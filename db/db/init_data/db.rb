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

COURSE_COUNT = 10
TEACHER_COUNT = 10
ASSISTANT_COUNT = 12
STUDENT_COUNT = 12
AS_COUNT = 5
NOTIFICATION_COUNT = 2
HOMEWORK_COUNT = 2

name_reader = NameReader.new

# create default admin and default avator
admin0 = User.create(name:'admin', phone:'0123', email:'a@b.c', password:'123', password_confirmation:'123',
                     student_number: '1230', description: '123', is_admin: true, avator_id: 1)
default_avator = FileResource.create(path: '/uploads/default_avator.jpg', name: 'default_avator.jpg', file_type: FILE_TYPE_IMAGE, creator_id: admin0.id)
admin0.avator_id = default_avator.id
admin0.save
puts 'admin created...'

# create files
files = []
files.push FileResource.create(name: '1.jpg', file_type: FILE_TYPE_IMAGE, path: '/uploads/1.jpg', creator_id: admin0.id)
files.push FileResource.create(name: '2.jpg', file_type: FILE_TYPE_IMAGE, path: '/uploads/2.jpg', creator_id: admin0.id)
puts 'files created...'

# create semesters
semesters = []
SEMESTER_COUNT.times do |x|
  semesters.push(
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_FALL),
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_SPRING),
      Semester.create(name: (SEMESTER_BEGIN+x).to_s + SEMESTER_SUMMER)
  )
end
puts 'semesters created...'

# create courses and lessons
start_time = Time.new(2000, 1, 1, 8, 0, 0)
courses = []
course_builder = CourseBuilder.new
COURSE_COUNT.times do
  course = Course.create(name: course_builder.build,
                         description: '基础课程',
                         semester_id: semesters[0].id)
  i = 0
  CourseBuilder.build_lessons.each do |lesson_name|
    lesson = course.lessons.create(name: lesson_name,
                          start_time: start_time + (2+i).hours,
                          end_time: start_time + (3+i).hours,
                          location: LocationBuilder.build)
    lesson.attached_files << files.sample
    lesson.save
    i += 1
  end
  courses.push(course)
end
puts 'courses and lessons created...'

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

# insert teachers into courses
courses.each do |course|
  teachers.each do |teacher|
    TeachingCourse.create(user_id: teacher.id, course_id: course.id)
  end
end
puts 'teachers created...'

# create messages
# notification
courses.each do |course|
  NOTIFICATION_COUNT.times do |x|
    course.messages.create(
        title: MessageBuilder.build_notification,
        content: '同学们千万不要忘了!',
        deadline: MessageBuilder.build_time,
        creator: teachers[0],
        message_type: :notification)
  end
end
puts 'notifications created...'

# homework
courses.each do |course|
  HOMEWORK_COUNT.times do
    course.messages.create(
        title: MessageBuilder.build_homework,
        content: '请同学们尽快完成',
        deadline: MessageBuilder.build_time,
        creator: teachers[0],
        message_type: :homework)
  end
end
puts 'homework created...'

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
  courses.each do |course|
    Participation.create(user_id: user.id, course_id: course.id, role: ROLE_ASSISTANT)
  end
end
puts 'assistants created...'

# create students for courses
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
  students.push(user)
  courses.each do |course|
    Participation.create(user_id: user.id, course_id: course.id, role: ROLE_STUDENT)
    course.lessons.each do |lesson|
      lesson.student_files.create(student_id: user.id, creator_id: user.id, file_resource_id: files.sample.id)
    end
  end
end
puts 'students created...'

# create lesson_comments and student_comments for student
students.each do |student|
  # 学生在每门课程上的每门实验课上被评分, 给实验课评分, 有一定概率签到
  student.courses.each do |course|
    course.lessons.each do |lesson|
      course_teachers = course.teachers
      teacher = course_teachers[Random.rand(course_teachers.size)]
      lesson.lesson_comments.create(score: Random.rand(0..10),
                                    content: '这个课程真好呀!',
                                    creator_id: student.id)
      lesson.student_comments.create(creator_id: teacher.id,
                                     student_id: student.id,
                                     score: Random.rand(0..100),
                                     content: '实验做的不错')
      if Random.rand(3) == 1
        lesson.student_attendences.create(user_id: student.id,
                                          lesson_id: lesson.id,
                                          sign_up_method: 'GPS',
                                          description: '40,166')
      end
    end
  end
end
puts 'student comments and lesson comments for students created...'

# create student and assistant
assistant_students = {}
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
  assistant_courses = []
  student_courses = []
  courses.each do |course|
    if course.id % 2 == 0
      Participation.create(user_id: user.id, course_id: course.id, role: ROLE_ASSISTANT)
      assistant_courses.push(course)
    else
      Participation.create(user_id: user.id, course_id: course.id, role: ROLE_STUDENT)
      student_courses.push(course)
    end
  end
  assistant_students[user] = { student_courses: student_courses, assistant_courses: assistant_courses }
end
puts 'assistant and student created...'

assistant_students.each do |user, his_courses|
  # 作为助教时, 什么都不用做
  # 作为学生时, 给每一个实验评分, 而且在每门课上被老师评分
  his_courses[:student_courses].each do |course|
    course.lessons.each do |lesson|
      teacher = teachers[Random.rand(teachers.size)]
      lesson.lesson_comments.create(score: Random.rand(10),
                                    content: '这个课程真好呀!',
                                    creator_id: user.id)
      lesson.student_comments.create(creator_id: teacher.id,
                                     student_id: user.id,
                                     score: Random.rand(100),
                                     content: '实验做的不错')
    end
  end
end
puts 'assistant and student comments created...'

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