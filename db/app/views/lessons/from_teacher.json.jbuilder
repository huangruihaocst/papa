json.status STATUS_SUCCESS
json.lessons do
  json.array! @lessons do |lesson|
    puts lesson.inspect
    json.extract!   lesson, :id
    json.extract!   lesson, :name
    json.extract!   lesson, :start_time
    json.extract!   lesson, :end_time
    json.extract!   lesson, :location
    json.extract!   lesson, :latitude
    json.extract!   lesson, :longitude
    json.extract!   lesson, :radius
  end
end
