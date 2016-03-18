json.status STATUS_SUCCESS
json.messages do
  json.array!(@messages) do |message|
    json.extract! message, :id
  end
end
