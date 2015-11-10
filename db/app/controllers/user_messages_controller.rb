class UserMessagesController < ApplicationController

  # GET /messages.json
  def index
    user = check_login
    @user_messages = user.user_messages
                         .where(receiver_deleted: false)
  end

  # POST /users/1/messages.json
  def create
    user = check_login
    receiver = User.find(params[:user_id])
    raise RequestException.new(REASON_TOO_OFTEN) unless check_send_freq(user, receiver)

    user_message = UserMessage.create(sender_id: user.id, receiver_id: receiver.id,
                       title: params[:title], content: params[:content], status: MESSAGE_STATUS_UNREAD)
    if user_message && user_message.valid?
      json_successful(id: user_message.id)
    else
      json_failed
    end
  end

  # POST /messages/1/read.json
  def read
    message = UserMessage.find(params[:message_id])
    message.status = MESSAGE_STATUS_READ
    json_successful
  end

  # GET /messages/new_messages_count
  def new_messages_count
    user = check_login
    @user_messages = user.user_messages
                         .where(status: MESSAGE_STATUS_UNREAD)
                         .where(receiver_deleted: false)
  end

  # DELETE /messages/1.json
  def destroy
    user = check_login
    message = UserMessage.find(params[:id])
    if message.sender == message.receiver
      message.sender_deleted, message.receiver_deleted = true, true
    elsif message.sender == user
      message.sender_deleted = true
    elsif message.receiver == user
      message.receiver_deleted = true
    else
      json_failed(REASON_PERMISSION_DENIED)
      return
    end
    if message.save
      json_successful
    else
      json_failed
    end
  end

  protected
  def check_send_freq(sender, receiver)
    # TODO
    true
  end

end