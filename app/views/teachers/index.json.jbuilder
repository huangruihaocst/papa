json.status STATUS_SUCCESS
json.teachers do
  json.array!(@teachers) do |teacher|
    json.extract! teacher, :id
    json.extract! teacher, :name
    json.extract! teacher, :email
    json.extract! teacher, :phone
  end
end