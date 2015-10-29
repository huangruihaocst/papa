s0 = Semester.create(name: '2015')
s1 = Semester.create(name: '2014')
s2 = Semester.create(name: '2013')

c1 = Course.create(name: 'os', description: '123', semester_id: s0.id)
c2 = Course.create(name: 'ds', description: '123', semester_id: s0.id)
c3 = Course.create(name: 'as', description: '123', semester_id: s0.id)
c4 = Course.create(name: 'cs', description: '123', semester_id: s1.id)

l11 = c1.lessons.create(name: 'os-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l12 = c1.lessons.create(name: 'os-l2', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l13 = c1.lessons.create(name: 'os-l3', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l14 = c1.lessons.create(name: 'os-l4', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')

l21 = c2.lessons.create(name: 'ds-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l22 = c2.lessons.create(name: 'ds-l2', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l23 = c2.lessons.create(name: 'ds-l3', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l24 = c2.lessons.create(name: 'ds-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')

l31 = c3.lessons.create(name: 'as-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l32 = c3.lessons.create(name: 'as-l2', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l33 = c3.lessons.create(name: 'as-l3', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l34 = c3.lessons.create(name: 'as-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')



admin0 = User.create(name:'admin', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123',
                 student_number: '1230', class_name: '44', department: 'cst', description: '123', is_admin: true)
default_avator = FileResource.create(path: 'default_avator.jpg', name: 'default_avator.jpg', file_type: 'jpg', creator_id: admin0.id)
admin0.avator_id = default_avator.id
puts default_avator.inspect
admin0.save

teacher0 = User.create(name:'alex', phone:'1234', email:'aa@b.c', password:'123', password_confirmation:'123',
                 student_number: '1231', class_name: '44', department: 'cst', description: '123', is_admin: false, is_teacher: true, avator_id: default_avator.id)
u2 = User.create(name:'betty', phone:'222', email:'b@c.d', password:'123', password_confirmation:'123',
                 student_number: '1232', class_name: '44', department: 'cst', description: '123', is_admin: false, avator_id: default_avator.id)
u3 = User.create(name:'ciara', phone:'333', email:'c@d.e', password:'123', password_confirmation:'123',
                 student_number: '1233', class_name: '44', department: 'cst', description: '123', is_admin: false, avator_id: default_avator.id)
u4 = User.create(name:'delta', phone:'444', email:'d@d.e', password:'123', password_confirmation:'123',
                 student_number: '1234', class_name: '44', department: 'cst', description: '123', is_admin: false, avator_id: default_avator.id)
u5 = User.create(name:'gamma', phone:'555', email:'e@d.e', password:'123', password_confirmation:'123',
                 student_number: '1235', class_name: '44', department: 'cst', description: '123', is_admin: false, avator_id: default_avator.id)
u6 = User.create(name:'yura', phone:'666', email:'f@d.e', password:'123', password_confirmation:'123',
                 student_number: '1236', class_name: '44', department: 'cst', description: '123', is_admin: false, avator_id: default_avator.id)

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

f1 = FileResource.create(name: '1.jpg', file_type: 'jpg', path: '1.jpg', creator_id: teacher0.id)
f2 = FileResource.create(name: '2.jpg', file_type: 'jpg', path: '2.jpg', creator_id: teacher0.id)

l11.student_files.create(student_id: u3.id, file_resource_id: f1.id)

l11.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '123', content: 'haha')
l11.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: 'haha1')
l11.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: 'haha2')
l11.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: 'haha3')

l12.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '123', content: 'haha')
l12.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: 'haha1')
l12.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: 'haha2')
l12.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: 'haha3')

l21.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '123', content: 'haha')
l21.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: 'haha1')
l21.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: 'haha2')
l21.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: 'haha3')

l31.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '123', content: 'haha')
l31.student_comments.create(creator_id: u2.id, student_id: u5.id, score: '333', content: 'haha1')
l31.student_comments.create(creator_id: teacher0.id, student_id: u4.id, score: '111', content: 'haha2')
l31.student_comments.create(creator_id: u3.id, student_id: u5.id, score: '222', content: 'haha3')

l11.lesson_comments.create(score: '123', content: 'haha', creator_id: u4.id)
l11.lesson_comments.create(score: '123', content: 'hahaha', creator_id: u3.id)

puts '--- init_data created ---'