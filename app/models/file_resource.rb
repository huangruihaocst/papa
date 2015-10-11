class FileResource < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  # string type, string name, string path, timestamps
end
