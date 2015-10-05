
json.status STATUS_SUCCESS
json.courses do
  json.array!(@courses) do |course|
    json.extract! course, :id
  end
end

