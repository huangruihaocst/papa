s0 = Semester.create(name: '2015')
s1 = Semester.create(name: '2014')
s2 = Semester.create(name: '2013')

c1 = Course.create(name: 'os', description: '123', semester_id: s0.id)
c2 = Course.create(name: 'ds', description: '123', semester_id: s0.id)
c3 = Course.create(name: 'as', description: '123', semester_id: s0.id)
c4 = Course.create(name: 'cs', description: '123', semester_id: s1.id)

l1 = c1.lessons.create(name: 'os-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l2 = c1.lessons.create(name: 'os-l2', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l3 = c1.lessons.create(name: 'os-l3', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')
l4 = c2.lessons.create(name: 'ds-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')

u0 = User.create(name:'admin', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123', is_admin: true)
u1 = User.create(name:'alex', phone:'1234', email:'aa@b.c', password:'123', password_confirmation:'123', is_teacher: true)
u2 = User.create(name:'betty', phone:'222', email:'b@c.d', password:'123', password_confirmation:'123')
u3 = User.create(name:'ciara', phone:'333', email:'c@d.e', password:'123', password_confirmation:'123')
u4 = User.create(name:'delta', phone:'444', email:'d@d.e', password:'123', password_confirmation:'123')
u5 = User.create(name:'gamma', phone:'555', email:'e@d.e', password:'123', password_confirmation:'123')

TeachingCourse.create(user_id: u1.id, course_id: c1.id)
Participation.create(user_id: u2.id, course_id: c1.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u3.id, course_id: c1.id, role: ROLE_ASSISTANT)
Participation.create(user_id: u4.id, course_id: c1.id, role: ROLE_STUDENT)
Participation.create(user_id: u5.id, course_id: c1.id, role: ROLE_STUDENT)

LessonStatus.create(user_id: u4.id, lesson_id: l1.id, creator_id: u2.id, score: 123)
LessonStatus.create(user_id: u5.id, lesson_id: l1.id, creator_id: u2.id, score: 111)

puts '--- init_data created ---'