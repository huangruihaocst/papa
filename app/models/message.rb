class Message < ActiveRecord::Base
  belongs_to :creator, class_name: 'User'
  belongs_to :course

  # string type, string title, datetime deadline, text content, timestamps
end
