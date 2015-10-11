require 'test_helper'

class CoursesControllerTest < ActionController::TestCase

  test 'api should get courses by teacher' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    get :index, format: :json, teacher_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal STATUS_SUCCESS, json['status']
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

  test 'api should not get courses by student if time out' do
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

  test 'api should create course by teacher' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    assert_difference 'TeachingCourse.count' do
      post :create, teacher_id: u.id, token: token_str, course: { name: 'test course', description: 'new' }
    end
  end
end
