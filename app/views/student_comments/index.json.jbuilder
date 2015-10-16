json.status STATUS_SUCCESS
json.student_comments do
  json.array!(@student_comments) do |student_comment|
    json.extract! student_comment, :id
    json.extract! student_comment, :content
    json.extract! student_comment, :score
    json.extract! student_comment, :creator_id
  end
end

