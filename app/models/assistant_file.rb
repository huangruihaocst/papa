class AssistantFile < ActiveRecord::Base
  belongs_to :assistant, class_name: 'User'
  belongs_to :lesson
  belongs_to :file_resource
end