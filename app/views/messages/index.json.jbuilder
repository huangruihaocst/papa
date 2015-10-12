json.status STATUS_SUCCESS
json.messages do
  json.array!(@messages) do |message|
    json.extract! message, :id
    json.extract! message, :title
  end
end
