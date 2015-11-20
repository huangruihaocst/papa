class Message < ActiveRecord::Base
  belongs_to :creator, class_name: 'User', dependent: :destroy
  belongs_to :course, dependent: :destroy

  # string type, string title, datetime deadline, text content, timestamps

  validates :course, presence: true
  validates :creator, presence: true
  validates :message_type, presence: true, inclusion: { in: %w"homework notification" }
  validates :title, presence: true
  validates :deadline, presence: true
end
