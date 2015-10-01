require 'test_helper'

class LessonsControllerTest < ActionController::TestCase

  test 'api should get all lessons' do
    get :api_index
    json = JSON.parse(@response.body)
    assert_equal json['status'], 'success'
    assert json['ids'].count > 0
  end

  test 'api should get lesson by id' do
    get :api_show, { id: Lesson.first.id }
    json = JSON.parse(@response.body)
    assert_equal json['status'], 'success'
    assert_not_nil json['result']['name']
  end

  test 'api should add lesson' do
    assert_difference('Lesson.count') do
      post :api_add, lesson: {
                       name: 'test lesson',
                       start_time: '2015/10/10 10:20:30',
                       end_time: '2015/10/10 10:20:30',
                       position: '"name": "here"',
                       course_id: 1
                   }
    end
    assert_equal JSON.parse(@response.body)['status'], 'success'
  end

  test 'api should not add bad lesson' do

    assert_no_difference('Lesson.count') do
      post :api_add, lesson: { bad_name: 'bad name' }
    end
    assert_equal JSON.parse(@response.body)['status'], 'fail'
  end


end
