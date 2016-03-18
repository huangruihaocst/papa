class StudentAttendence < ActiveRecord::Base
  belongs_to :user
  belongs_to :lesson

  # timestamps, sign_up_method, description
end