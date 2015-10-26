json.status STATUS_SUCCESS
json.files do
  json.array! @files do |file|
    json.id    file.id
    json.type  file.file_type
  end
end