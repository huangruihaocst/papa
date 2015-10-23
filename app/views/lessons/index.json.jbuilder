
json.status STATUS_SUCCESS
json.lessons do
  json.array!(@lessons) do |lesson|
    json.extract! lesson, :id
    json.extract! lesson, :name
    json.extract! lesson, :description
    json.extract! lesson, :start_time
    json.extract! lesson, :end_time
    json.extract! lesson, :location
    json.extract! lesson, :course_id
  end
end

