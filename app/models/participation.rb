class Participation < ActiveRecord::Base

  validates :role, presence: true
  validates_associated :user
  validates_associated :course

  belongs_to :user
  belongs_to :course

end