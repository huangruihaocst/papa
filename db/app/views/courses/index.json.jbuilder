
json.status STATUS_SUCCESS
if @courses
  json.courses do
    json.array!(@courses) do |course|
      json.extract! course, :id
      json.extract! course, :name
      json.extract! course, :semester_id
      json.extract! course, :description
    end
  end
end

if @student_courses
  json.student_courses do
    json.array!(@student_courses) do |course|
      json.extract! course, :id
      json.extract! course, :name
      json.extract! course, :semester_id
      json.extract! course, :description
    end
  end
end

if @assistant_courses
  json.assistant_courses do
    json.array!(@assistant_courses) do |course|
      json.extract! course, :id
      json.extract! course, :name
      json.extract! course, :semester_id
      json.extract! course, :description
    end
  end
end


