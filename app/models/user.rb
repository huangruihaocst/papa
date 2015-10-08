class User < ActiveRecord::Base

  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  # phone, name, email
  has_many :participations
  has_many :courses, through: :participations
  has_many :tokens

  # the following 4 methods allow us to login with phone or email
  def login=(login)
    @login = login
  end
  def login

    @login || self.phone || self.email
  end
  def self.find_for_database_authentication(warden_conditions)
    conditions = warden_conditions.dup
    if login = conditions.delete(:login)
      where(conditions.to_hash).where(["lower(phone) = :value OR lower(email) = :value", { :value => login.downcase }]).first
    else
      where(conditions.to_hash).first
    end
  end
  def email_required?
    false
  end

  # for token authentication
  def create_token
    Token.create(user_id: id, token: Token.safe_token, valid_until: Time.now + TOKEN_VALID_TIME_SEC)
  end

end
