json.status STATUS_SUCCESS

json.message do
  json.id       @message.id
  json.title    @message.title
  json.content  @message.content
  json.type     @message.message_type
  json.deadline @message.deadline
  json.creator_name @message.creator.name
end