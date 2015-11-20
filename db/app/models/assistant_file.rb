class AssistantFile < ActiveRecord::Base
  belongs_to :assistant, class_name: 'User', dependent: :destroy
  belongs_to :lesson, dependent: :destroy
  belongs_to :file_resource, dependent: :destroy
end