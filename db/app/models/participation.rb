class Participation < ActiveRecord::Base

  belongs_to :user, dependent: :destroy
  belongs_to :course, dependent: :destroy

  validates :role, presence: true
  validates :user, presence: true
  validates :course, presence: true

end