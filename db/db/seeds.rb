# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
#require './db/init_data/db'

# create default admin and default avator
admin0 = User.create(name:'admin', phone:'0123', email:'a@b.c', password:'123', password_confirmation:'123',
                     student_number: '1230', description: '123', is_admin: true, avator_id: 1)
default_avator = FileResource.create(path: '/uploads/default_avator.jpg', name: 'default_avator.jpg', file_type: FILE_TYPE_IMAGE, creator_id: admin0.id)
admin0.avator_id = default_avator.id
admin0.save
puts 'admin created...'

