require 'test_helper'

class UserMessagesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  # GET /messages.json
  test 'should get all message of current user' do
    user = User.first
    sign_in user

    get :index, format: :json
    assert_json_success
    assert_not_nil json['user_messages']
  end

  # POST /users/1/messages.json
  test 'should send message to user' do
    sender = User.first
    receiver = User.last
    sign_in sender

    post :create, format: :json,
         user_id: receiver.id,
         title: 'title',
         content: 'content'
    assert_json_success
    assert_not_nil json['id']
  end

  # DELETE /messages/1.json
  test 'should delete user message' do
    user = User.find_by_name('betty')
    sign_in user

    delete :destroy, format: :json, id: user.user_messages.first.id
    assert_json_success
  end

  # DELETE /messages/1.json
  test 'should not delete user message without privilege' do
    user = User.find_by_name('deng')
    betty = User.find_by_name('betty')
    sign_in user

    delete :destroy, format: :json, id: betty.user_messages.first.id
    assert_json_status STATUS_FAIL
  end

  # GET /messages/new_messages_count
  test 'should get new message count' do
    user = User.find_by_name('betty')
    sign_in user

    get :new_messages_count, format: :json
    assert_json_success
  end

  # POST /messages/1/read.json
  test 'should set a message as read' do
    post :read, format: :json, message_id: UserMessage.first.id
    assert_json_success
  end

end