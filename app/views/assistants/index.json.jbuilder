
json.status STATUS_SUCCESS
json.assistants do
  json.array!(@assistants) do |assistant|
    json.extract! assistant, :id
    json.extract! assistant, :name
    json.extract! assistant, :phone
    json.extract! assistant, :email
  end
end

