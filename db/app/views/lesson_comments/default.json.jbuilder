json.status STATUS_SUCCESS

json.lesson_comment do
  json.id       @lesson_comment.id
  json.content  @lesson_comment.content
  json.score    @lesson_comment.score
  json.creator_name @lesson_comment.creator.name
end