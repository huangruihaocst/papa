json.status STATUS_SUCCESS
json.user_messages do
  json.array! @user_messages do |user_message|
    json.extract! user_message, :id
    puts user_message
    json.sender_id user_message.sender.id
    json.sender_name user_message.sender.name
    json.extract! user_message, :title
    json.extract! user_message, :content
    json.extract! user_message, :status
    json.extract! user_message, :created_at
  end
end
