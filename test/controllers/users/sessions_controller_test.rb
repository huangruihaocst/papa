require 'test_helper'

class Users::SessionsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  def setup
    @request.env["devise.mapping"] = Devise.mappings[:user]
  end

  test 'should sign in with phone' do
    post :create, { format: :json, user: { login: '123', password: '123' } }
    assert_equal assigns[:user].phone, '123'
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
  end

  test 'should sign in with email' do
    post :create, { user: { login: 'alex@b.c', password: '123' } }
    assert_equal assigns[:user].email, 'alex@b.c'
  end

  test 'should sign out and returns json' do
    sign_in User.first
    delete :destroy, { format: :json }
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
  end

end