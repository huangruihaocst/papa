require 'test_helper'

class CoursesControllerTest < ActionController::TestCase

  test 'api should get all courses' do
    get :api_index
    json = JSON.parse(@response.body)
    assert_equal json['status'], 'success'
    assert json['ids'].count > 0
  end

  test 'api should get course by id' do
    get :api_show, { id: Course.first.id }
    json = JSON.parse(@response.body)
    assert_equal json['status'], 'success'
    assert_not_nil json['result']['name']
  end

  test 'api should add course' do
    assert_difference 'Course.count' do
      post :api_add, course: { name: 'test course' }
    end
  end

  test 'api should not add bad course' do
    assert_no_difference 'Course.count' do
      post :api_add, course: { name1: 'bad name' }
    end
  end
end
