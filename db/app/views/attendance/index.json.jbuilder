json.status STATUS_SUCCESS
json.students do
  json.array!(@students) do |student|
    json.student_info do
      json.extract! student, :id
      json.extract! student, :name
    end
  end
end
