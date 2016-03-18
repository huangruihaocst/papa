class AndroidApp < ActiveRecord::Base
  belongs_to :file_resource
  # string version, timestamps
end
