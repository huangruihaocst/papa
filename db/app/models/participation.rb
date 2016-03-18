class Participation < ActiveRecord::Base

  belongs_to :user
  belongs_to :course

  validates :role, presence: true
  validates :user, presence: true
  validates :course, presence: true

end