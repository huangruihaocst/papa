json.status STATUS_SUCCESS
json.student_comment do
  json.id @student_comment.id
  json.content @student_comment.content
  json.creator_id @student_comment.creator_id
  json.score @student_comment.score
end