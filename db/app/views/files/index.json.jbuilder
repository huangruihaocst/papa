json.status STATUS_SUCCESS
json.files do
  json.array! @files do |file|
    json.id     file.id
    json.type   file.file_type
    json.name   file.name
    json.path   file.path
    json.created_at file.created_at
  end
end