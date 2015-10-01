class Course < ActiveRecord::Base
  validates :name, presence: true

  has_many :lessons
  has_many :participants
end
