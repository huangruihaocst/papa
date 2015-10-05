
json.status STATUS_SUCCESS
json.lessons do
  json.array!(@lessons) do |lesson|
    json.extract! lesson, :id
  end
end

