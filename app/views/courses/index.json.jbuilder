
json.status STATUS_SUCCESS
json.courses do
  json.array!(@courses) do |course|
    json.extract! course, :id
    json.extract! course, :name
  end
end

