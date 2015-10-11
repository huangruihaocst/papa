require 'test_helper'

class LessonsControllerTest < ActionController::TestCase

  test 'api should get all lessons' do
    get :index, format: :json, course_id: 1
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['lessons'].count > 0
  end

  test 'api should get lesson by id' do
    get :show, format: :json, id: Lesson.first.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['lesson']['name']
  end

  test 'api should add lesson' do
    assert_difference('Lesson.count') do
      post :create, format: :json, course_id: 1, lesson: {
                      name: 'test lesson',
                      start_time: '2015/10/10 10:20:30',
                      end_time: '2015/10/10 10:20:30',
                      location: '"name": "here"'
                  }
    end
    assert_equal JSON.parse(@response.body)['status'], STATUS_SUCCESS
  end

  test 'api should not add bad lesson' do
    assert_no_difference('Lesson.count') do
      post :create, format: :json, course_id: 1, lesson: { bad_name: 'bad name' }
    end
    assert_equal JSON.parse(@response.body)['status'], STATUS_FAIL
  end


end
