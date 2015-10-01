class Lesson < ActiveRecord::Base
  validates :name,        presence: true
  # description, nullable
  validates :start_time,  presence: true
  validates :end_time,    presence: true
  validates :position,    presence: true

  belongs_to :courses
end
