json.status STATUS_SUCCESS
json.files do
  json.array! @files do |file|
    json.id     file.id
    json.type   file.file_type
    json.name   file.name
    json.path   file.path
    json.created_at file.created_at

    if file.student_file
      json.creator_name file.student_file.creator.name
      json.creator_id file.student_file.creator.id
    elsif file.assistant_file
      json.creator_name file.assistant_file.creator.name
      json.creator_id file.assistant_file.creator.id
    else
      json.creator_name file.creator.name
      json.creator_id file.creator.id
    end
  end
end