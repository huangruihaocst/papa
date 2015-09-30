class Participation < ActiveRecord::Base

  validates :role, presence: true
  validates_associated :users
  validates_associated :courses

  belongs_to :users
  belongs_to :courses

end