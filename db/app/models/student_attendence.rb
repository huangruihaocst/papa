class StudentAttendence < ActiveRecord::Base
  belongs_to :user
  belongs_to :lesson

  # timestamps, method, description
end