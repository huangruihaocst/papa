class UserMessage < ActiveRecord::Base
  # title, content, status, timestamps, sender_deleted, receiver_deleted

  belongs_to :sender, class_name: 'User'
  belongs_to :receiver, class_name: 'User'
end