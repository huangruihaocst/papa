
json.status STATUS_SUCCESS
json.assistant do
  json.id     @assistant.id
  json.name   @assistant.name
  json.phone  @assistant.phone
  json.email  @assistant.email
end