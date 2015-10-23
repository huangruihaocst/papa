json.status STATUS_SUCCESS
json.course do
  json.id     @course.id
  json.name   @course.name
  json.semester_id @course.semester_id
  json.description @course.description
end
