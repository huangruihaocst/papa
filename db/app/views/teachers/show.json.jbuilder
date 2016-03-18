json.status STATUS_SUCCESS
json.teacher do
  json.id     @teacher.id
  json.name   @teacher.name
  json.phone  @teacher.phone
  json.phone  @teacher.email
  json.avator_id   @teacher.avator_id
end