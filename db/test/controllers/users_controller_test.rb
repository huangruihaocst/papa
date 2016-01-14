require 'test_helper'

class UsersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE

  # GET /users/current.json
  test 'should get current user' do
    user = User.first
    sign_in user

    get :current, format: :json
    assert_json_success
    assert_equal json['id'].to_i, user.id
  end

  # GET /users/current.json
  test 'should not get current user if not signed in' do
    get :current, format: :json
    assert_equal STATUS_FAIL, json['status']
  end

  # GET /users/1.json
  test 'should get one user by id' do
    get :show, format: :json, id: User.first.id
    assert_json_success
  end

  # GET /users/1.json
  test 'should not get one user if not exists' do
    get :show, format: :json, id: -1
    assert_json_status STATUS_FAIL
  end

  # PUT /users/1.json
  test 'should update user information' do
    user = User.first
    sign_in user
    put :update, format: :json, id: user.id, user: {
        phone: '123124',
        email: 'aasdfsdf@adc.c',
        name: 'alexfuck',
        password: '123',
        password_confirmation: '123'
    }
    assert_json_success
  end

end
