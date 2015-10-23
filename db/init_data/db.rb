s = Semester.create(name: '2015')

c1 = Course.create(name: 'os', description: '123', semester_id: s.id)
l1 = c1.lessons.create(name: 'os-l1', start_time: Time.now, end_time: Time.now + 100.years, location: '5201')


c2 = Course.create(name: 'ds', description: '中文', semester_id: s.id)
c3 = Course.create(name: '语文', description: 'fuck', semester_id: s.id)

u1 = User.create(name:'alex', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123', is_teacher: true)
u1.save

TeachingCourse.create(user_id: u1.id, course_id: c1.id)

u2 = User.create(name:'betty', phone:'222', email:'b@c.d', password:'123', password_confirmation:'123')
u3 = User.create(name:'ciara', phone:'333', email:'c@d.e', password:'123', password_confirmation:'123')


p11 = Participation.create(user_id: u1.id, course_id: c1.id, role: ROLE_STUDENT)
lesson_status1 = LessonStatus.create(user_id: u1.id, lesson_id: c1.lessons.first.id, creator_id: u1.id, score: 123)


p12 = Participation.create(user_id: u1.id, course_id: c2.id, role: ROLE_ASSISTANT)
p21 = Participation.create(user_id: u2.id, course_id: c1.id, role: ROLE_STUDENT)
p22 = Participation.create(user_id: u2.id, course_id: c2.id, role: ROLE_STUDENT)
p32 = Participation.create(user_id: u3.id, course_id: c3.id, role: ROLE_STUDENT)
p33 = Participation.create(user_id: u3.id, course_id: c3.id, role: ROLE_STUDENT)

