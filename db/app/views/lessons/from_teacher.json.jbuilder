json.status STATUS_SUCCESS
json.lessons do
  json.array! @lessons do |lesson|
    json.id         lesson.id
    json.name       lesson.name
    json.start_time lesson.start_time
    json.end_time   lesson.end_time
    json.location   lesson.location
    json.latitude   lesson.latitude
    json.longitude  lesson.longitude
    json.radius     lesson.radius
  end
end
