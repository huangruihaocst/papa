json.status STATUS_SUCCESS
json.file do
  json.id   @file.id
  json.name @file.name
  json.type @file.file_type
  json.path @file.path
  json.created_at @file.created_at
end