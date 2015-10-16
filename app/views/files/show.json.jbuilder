json.status STATUS_SUCCESS
json.file do
  json.id   @file.id
  json.name @file.name
  json.type @file.type
  json.path @file.path
end