require 'test_helper'

class CoursesControllerTest < ActionController::TestCase

  test 'api should get all courses' do
    get :index, format: :json
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['courses'].count > 0
  end

  test 'api should get courses by assistant with token' do
    u = User.find_by_name('betty')
    token_str = u.create_token.token

    get :index, format: :json, assistant_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['courses']
  end

  test 'api should get courses by student with token' do
    u = User.first
    token_str = u.create_token.token

    get :index, format: :json, student_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['courses']
  end

  test 'api should not get courses by student with invalid token' do
    u = User.first

    get :index, format: :json, student_id: u.id, token: 'invalid token'
    json = JSON.parse(@response.body)
    assert_equal STATUS_FAIL, json['status']
    assert_equal json['reason'], REASON_TOKEN_INVALID
  end

  test 'api should not get courses if time out' do
    u = User.first
    # create an invalid token
    token = Token.create(user_id: u.id, token: rand(TOKEN_MAX_RAND).to_s, valid_until: Time.now - 200)

    get :index, format: :json, student_id: u.id, token: token.token
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_FAIL
    assert_equal json['reason'], REASON_TOKEN_TIMEOUT
  end

  test 'api should get course by id' do
    get :show, format: :json, id: Course.first.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['course']['name']
  end

  test 'api should not get course by invalid id' do
    get :show, format: :json, id: -1
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_FAIL
  end

  test 'api should create course' do
    assert_difference 'Course.count' do
      post :create, course: { name: 'test course' }
    end
  end

  test 'api should not create bad course' do
    assert_no_difference 'Course.count' do
      post :create, format: :json, course: { name1: 'bad name' }
    end
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_FAIL
  end

end
