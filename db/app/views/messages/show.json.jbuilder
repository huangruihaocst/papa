json.status STATUS_SUCCESS
json.message do
  json.id @message.id
  json.title @message.title
  json.deadline @message.deadline
  json.content @message.content
  json.creator_id @message.creator_id
  json.creator_name @message.creator.name
  json.message_type @message.message_type
  json.course_id @message.course_id
end