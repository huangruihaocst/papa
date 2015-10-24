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

end
