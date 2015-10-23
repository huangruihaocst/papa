json.status STATUS_SUCCESS
json.student do
  json.id     @student.id
  json.name   @student.name
  json.phone  @student.phone
  json.email  @student.email
end