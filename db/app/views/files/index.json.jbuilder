json.status STATUS_SUCCESS
json.files do
  json.array! @files do |file|
    json.extract! file, :id
  end
end