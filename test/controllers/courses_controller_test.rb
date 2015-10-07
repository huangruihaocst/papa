require 'test_helper'

class CoursesControllerTest < ActionController::TestCase

  test 'api should get all courses' do
    get :index, format: :json
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['courses'].count > 0
  end

  test 'api should get course by id' do
    get :show, format: :json, id: Course.first.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['course']['name']
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
