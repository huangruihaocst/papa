json.status STATUS_SUCCESS
json.students do
  json.array!(@students) do |student|
    json.extract! student, :id
    json.extract! student, :name
    json.extract! student, :phone
  end
end

