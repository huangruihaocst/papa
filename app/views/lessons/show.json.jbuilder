
json.status STATUS_SUCCESS
json.lesson do
  json.id         @lesson.id
  json.name       @lesson.name
  json.start_time @lesson.start_time
  json.end_time   @lesson.end_time
  json.location   @lesson.location
end
