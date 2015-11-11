
# create semesters
SEMESTER_BEGIN = 2008
SEMESTER_COUNT = 10
SEMESTER_SPRING = '春季'
SEMESTER_SUMMER = '夏季'
SEMESTER_FALL = '秋季'

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
course_names = {
    '操作系统' => ['实验1', '实验2', '实验3', '实验4'],
    '计算机组成原理' => ['实验1', '实验2', '实验3', '实验4'],
    '程序设计基础' => ['实验1', '实验2', '实验3', '实验4'],
    '离散数学' => ['实验1', '实验2', '实验3', '实验4']
}
start_time = Time.new(2000, 1, 1, 8, 0, 0)

courses = []
course_names.each do |course_name, lesson_names|
  course = Course.create(name: course_name,
                         description: course_name + ', 计算机基础课程',
                         semester_id: semesters[0].id)
  i = 0
  lesson_names.each do |lesson_name|
    course.lessons.create(name: lesson_name,
                          start_time: start_time + (2+i).hours,
                          end_time: start_time + (3+i).hours,
                          location: '五教5201')
    i += 1
  end
  courses.push(course)

end
puts 'courses created...'

# create default admin and default avator
admin0 = User.create(name:'admin', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123',
                 student_number: '1230', class_name: '44', department: '计算机系', description: '123', is_admin: true, avator_id: 1)
default_avator = FileResource.create(path: '/uploads/default_avator.jpg', name: 'default_avator.jpg', file_type: 'jpg', creator_id: admin0.id)
admin0.avator_id = default_avator.id
admin0.save
puts 'admin created...'

# create teachers
teacher_names = ['张三', '张四', '王大卫']
teachers = []

i = 0
courses.each do |course|
  teacher = User.create(name: teacher_names[i], phone: "1#{i}", email: "teacher#{i}@b.c", password:'123', password_confirmation:'123',
                         student_number: "1#{i}", class_name: '4x', department: '计算机系', description: 'xxx', is_admin: false, is_teacher: true)
  TeachingCourse.create(user_id: teacher.id, course_id: course.id)

  i += 1
  i %= teacher_names.size
  teachers.push(teacher)
end
puts 'teachers created...'

# create assistant for course 0
assistant_names = %w"了数量 进口的 分类时 开机 的分类 开始 解放路 凯沙 罗顿发送"
assistant_names.each do |assistant_name|
  user =  User.create(name: assistant_name,
                      phone: "2#{i}",
                      email:"assistant#{i}@b.c",
                      password:'123', password_confirmation:'123',
                      student_number: "2#{i}",
                      class_name: '4x',
                      department: '计算机系',
                      description: 'xxx',
                      is_admin: false, is_teacher: false)
  Participation.create(user_id: user.id, course_id: courses[0].id, role: ROLE_ASSISTANT)
  i += 1
end
puts 'assistants created...'

# create students for course 0
student_names = %w'顺 路快 递非农 阿森纳费 哦司机 的愤怒 死定就分io s就发 送始发 地就'
students = []
i = 0
student_names.each do |student_name|
  user = User.create(name: student_name,
                     phone: "3#{i}",
                     email:"student#{i}@b.c",
                     password:'123', password_confirmation:'123',
                     student_number: "3#{i}",
                     class_name: '4x',
                     department: '计算机系',
                     description: '123',
                     is_admin: false, is_teacher: false)
  Participation.create(user_id: user.id, course_id: courses[0].id, role: ROLE_STUDENT)
  students.push(user)
  i += 1
end
puts 'students created...'

# create files
f1 = FileResource.create(name: '1.jpg', file_type: 'jpg', path: '/uploads/1.jpg', creator_id: admin0.id)
f2 = FileResource.create(name: '2.jpg', file_type: 'jpg', path: '/uploads/2.jpg', creator_id: admin0.id)
puts 'files created...'

## create lesson comments
#l11.student_files.create(student_id: u3.id, file_resource_id: f1.id)
LESSON_COMMENT_COUNT = 10
courses[0].lessons.each do |lesson|
  LESSON_COMMENT_COUNT.times do |x|
    lesson.course.students.each do |student|
      lesson.student_comments.create(creator_id: teachers[Random.rand(teachers.size)].id,
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
NOTIFICATION_COUNT = 5
courses.each do |course|
  NOTIFICATION_COUNT.times do |x|
    course.messages.create(
        title: "期中考试#{x}",
        content: '五教-5' + Random.rand(999).to_s,
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
        title: "作业#{x}",
        content: '实验xxxx报告',
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
                       content: '原因很复杂...',
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