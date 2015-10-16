json.status STATUS_SUCCESS
json.lesson_comments do
  json.array! @lesson_comments do |lesson_comment|
    json.extract! lesson_comment, :id
    json.extract! lesson_comment, :content
    json.extract! lesson_comment, :score
  end
end