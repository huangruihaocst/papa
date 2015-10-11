s = Semester.create(name: '2015')

c1 = Course.create(name: 'os', description: '123', semester_id: s.id)
c2 = Course.create(name: 'ds', description: '中文', semester_id: s.id)
c3 = Course.create(name: '语文', description: 'fuck', semester_id: s.id)

u1 = User.create(name:'alex', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123')
u2 = User.create(name:'betty', phone:'222', email:'b@c.d', password:'123', password_confirmation:'123')
u3 = User.create(name:'ciara', phone:'333', email:'c@d.e', password:'123', password_confirmation:'123')


p11 = Participation.create(user_id: u1.id, course_id: c1.id, role: ROLE_STUDENT)
p12 = Participation.create(user_id: u1.id, course_id: c2.id, role: ROLE_ASSISTANT)
p21 = Participation.create(user_id: u2.id, course_id: c1.id, role: ROLE_STUDENT)
p22 = Participation.create(user_id: u2.id, course_id: c2.id, role: ROLE_STUDENT)
p32 = Participation.create(user_id: u3.id, course_id: c3.id, role: ROLE_STUDENT)
p33 = Participation.create(user_id: u3.id, course_id: c3.id, role: ROLE_STUDENT)

