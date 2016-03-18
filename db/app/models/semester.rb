class Semester < ActiveRecord::Base
  has_many :courses, dependent: :destroy
  # string name
end
