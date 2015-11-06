s0 = Semester.create(name: '2015秋季学期')
s1 = Semester.create(name: '2015春季学期')
s2 = Semester.create(name: '2013秋季学期')

c1 = Course.create(name: '操作系统', description: '操作系统, 计算机基础课程', semester_id: s0.id)
c2 = Course.create(name: '数据结构', description: '数据结构, 计算机基础课程', semester_id: s0.id)
c3 = Course.create(name: '安卓开发', description: '教授android开发, 毕业后保证1000+每月', semester_id: s0.id)
c4 = Course.create(name: '竞技游戏导论', description: '通过教学CS游戏, 提高学生的竞技游戏能力', semester_id: s1.id)

l11 = c1.lessons.create(name: 'Bootstrap实验', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l12 = c1.lessons.create(name: '内存管理子系统实验', start_time: Time.now, end_time: Time.now + 100.days, location: '三教3201')
l13 = c1.lessons.create(name: 'IO子系统实验', start_time: Time.now, end_time: Time.now + 100.days, location: '三角3201')
l14 = c1.lessons.create(name: '终端处理实验', start_time: Time.now, end_time: Time.now + 100.days, location: '一教201')

l21 = c2.lessons.create(name: '实现二叉树实验', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l22 = c2.lessons.create(name: '红黑树复杂度实验', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l23 = c2.lessons.create(name: 'B树实验', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l24 = c2.lessons.create(name: '3min之内写出Dijkstra算法', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')

l31 = c3.lessons.create(name: 'Activity实践与探索', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l32 = c3.lessons.create(name: '大话Service', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l33 = c3.lessons.create(name: '深入浅出Broadcast', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')
l34 = c3.lessons.create(name: '30天开发JVM', start_time: Time.now, end_time: Time.now + 100.days, location: '五教5201')

admin0 = User.create(name:'admin', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123',
                 student_number: '1230', class_name: '44', department: '计算机系', description: '123', is_admin: true, avator_id: 1)
default_avator = FileResource.create(path: '/uploads/default_avator.jpg', name: 'default_avator.jpg', file_type: 'jpg', creator_id: admin0.id)
admin0.avator_id = default_avator.id
admin0.save

teacher0 = User.create(name:'张三', phone:'1234', email:'aa@b.c', password:'123', password_confirmation:'123',
                 student_number: '1231', class_name: '44', department: '计算机系', description: '123', is_admin: false, is_teacher: true)
u2 = User.create(name:'张四', phone:'222', email:'b@c.d', password:'123', password_confirmation:'123',
                 student_number: '1232', class_name: '44', department: '计算机系', description: '123', is_admin: false)
u3 = User.create(name:'张五', phone:'333', email:'c@d.e', password:'123', password_confirmation:'123',
                 student_number: '1233', class_name: '44', department: '计算机系', description: '123', is_admin: false)
u4 = User.create(name:'张六', phone:'444', email:'d@d.e', password:'123', password_confirmation:'123',
                 student_number: '1234', class_name: '44', department: '计算机系', description: '123', is_admin: false)
u5 = User.create(name:'张七', phone:'555', email:'e@d.e', password:'123', password_confirmation:'123',
                 student_number: '1235', class_name: '44', department: '计算机系', description: '123', is_admin: false)
u6 = User.create(name:'张八', phone:'666', email:'f@d.e', password:'123', password_confirmation:'123',
                 student_number: '1236', class_name: '44', department: '计算机系', description: '123', is_admin: false)

TeachingCourse.create(user_id: teacher0.id, course_id: c1.id)

Participation.create(user_id: u2.id, course_id: c1.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u2.id, course_id: c2.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u2.id, course_id: c3.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u2.id, course_id: c4.id, role: ROLE_ASSISTANT)

Participation.create(user_id: u3.id, course_id: c1.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u3.id, course_id: c2.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u3.id, course_id: c3.id, role: ROLE_STUDENT)
Participation.create(user_id: u3.id, course_id: c4.id, role: ROLE_STUDENT)

Participation.create(user_id: u4.id, course_id: c1.id, role: ROLE_STUDENT)
Participation.create(user_id: u4.id, course_id: c2.id, role: ROLE_STUDENT)
Participation.create(user_id: u4.id, course_id: c3.id, role: ROLE_STUDENT)
Participation.create(user_id: u4.id, course_id: c4.id, role: ROLE_STUDENT)

Participation.create(user_id: u5.id, course_id: c1.id, role: ROLE_STUDENT)
Participation.create(user_id: u6.id, course_id: c1.id, role: ROLE_STUDENT)

f1 = FileResource.create(name: '1.jpg', file_type: 'jpg', path: '/uploads/1.jpg', creator_id: teacher0.id)
f2 = FileResource.create(name: '2.jpg', file_type: 'jpg', path: '/uploads/2.jpg', creator_id: teacher0.id)

l11.student_files.create(student_id: u3.id, file_resource_id: f1.id)

l11.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '8', content: '这门实验课非常赞')
l11.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: 'Bootstrap还是不太懂')
l11.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: 'IO子系统是如何修改中断向量表的?')
l11.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: 'OS又崩了')

l12.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '7', content: '这实验真无聊,我幼儿园就会了')
l12.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: '我觉得这个实验有bug, 应该...')
l12.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: '明年不要开这门实验了,太难了')
l12.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: '好实验真赞^_^')

l21.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '6', content: '二叉树是什么鬼?')
l21.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: '我会100种树和1000种搜索算法')
l21.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: '我能30s内用c语言写出快排')
l21.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: '我能30min内造一台计算机')

l31.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '5', content: '哈哈, 老师真帅')
l31.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: '哈哈, 助教真帅')
l31.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: '哈哈安卓真好玩')
l31.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: '哈哈, 我再也不学安卓了')

l11.lesson_comments.create(score: '10', content: '这个课程真好呀!', creator_id: u4.id)
l11.lesson_comments.create(score: '1', content: '这个课程真无聊啊!', creator_id: u3.id)

c1.messages.create(title: '期中考试', content: '五教-5201', deadline: Time.now, creator: teacher0, message_type: :notification)
c1.messages.create(title: '期末考试', content: '五教-5202', deadline: Time.now + 3.days, creator: teacher0, message_type: :notification)
c1.messages.create(title: '第五周课程取消', content: '五教-5203', deadline: Time.now + 3.days, creator: teacher0, message_type: :notification)
c1.messages.create(title: '第三周作业上交', content: '选作', deadline: Time.now, creator: teacher0, message_type: :homework)
c1.messages.create(title: '第五周作业上交', content: '必做', deadline: Time.now + 1.days, creator: teacher0, message_type: :homework)

puts '--- init_data created ---'