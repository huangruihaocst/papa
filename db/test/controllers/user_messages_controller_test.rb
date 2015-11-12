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

end