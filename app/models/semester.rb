class Semester < ActiveRecord::Base
  has_many :courses
  # string name
end
