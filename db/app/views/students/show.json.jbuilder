json.status STATUS_SUCCESS
json.student do
  json.id     @student.id
  json.name   @student.name
  json.phone  @student.phone
  json.email  @student.email

  json.student_number   @student.student_number
  json.department       @student.department
  json.class_name       @student.class_name
  json.avator_id        @student.avator_id
  json.description      @student.description
end