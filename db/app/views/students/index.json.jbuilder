json.status STATUS_SUCCESS
json.students do
  json.array!(@students) do |student|
    json.extract! student, :id
    json.extract! student, :name
    json.extract! student, :phone
    json.extract! student, :email
    json.extract! student, :student_number
    json.extract! student, :department
    json.extract! student, :class_name
    json.extract! student, :avator_id
    json.extract! student, :description
  end
end

