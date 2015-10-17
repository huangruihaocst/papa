class Token < ActiveRecord::Base
  belongs_to :user

  validates :user, presence: true
  validates :valid_until, presence: true
  validates :token, presence: true

  def self.safe_token
    rand(TOKEN_MAX_RAND).to_s
  end

  def token_valid?
    valid_until > Time.now
  end

  def self.check_token(params, user_id)
    return REASON_TOKEN_INVALID unless params[:token]

    begin
      token = Token.find_by_token(params[:token])
      raise ActiveRecord::RecordNotFound unless token
      if token.user_id == user_id.to_i
        if token.valid_until > Time.now
          return STATUS_SUCCESS
        else
          return REASON_TOKEN_TIMEOUT
        end
      else
        return REASON_TOKEN_INVALID
      end
    rescue ActiveRecord::RecordNotFound
      return REASON_TOKEN_INVALID
    end
  end

end