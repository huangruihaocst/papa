json.status STATUS_SUCCESS
json.semesters do
  json.array!(@semesters) do |semester|
    json.extract! semester, :id
    json.extract! semester, :name
  end
end

