c = Course.create(name: 'os', description: '123')
u = User.create(name:'alex', phone:'123', email:'a@b.c', password:'123', password_confirmation:'123')
p = Participation.create(user_id: u.id, course_id: c.id)
u.participations << p
u.save

