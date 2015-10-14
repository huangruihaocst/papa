require 'test_helper'

class LessonsControllerTest < ActionController::TestCase

  def setup
    @course = Course.find_by_name('Operation System')
  end

  # GET /course/1/lessons.json
  test 'api should get all lessons by course' do
    get :index, format: :json, course_id: @course.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['lessons'].count > 0
  end

  # GET /lessons/1.json
  test 'api should get lesson by id' do
    get :show, format: :json, id: Lesson.first.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['lesson']['name']
  end

  # POSt /course/1/lessons.json
  test 'api should add lesson to course' do
    assert_difference('Lesson.count') do
      post :create, format: :json, course_id: @course.id, lesson: {
                      name: 'test lesson',
                      start_time: '2015/10/10 10:20:30',
                      end_time: '2015/10/10 10:20:30',
                      location: '"name": "here"'
                  }
    end
    assert_equal JSON.parse(@response.body)['status'], STATUS_SUCCESS
  end

  # POST /courses/1/lessons.json
  test 'api should not add bad lesson' do
    assert_no_difference('Lesson.count') do
      post :create, format: :json, course_id: @course.id, lesson: { bad_name: 'bad name' }
    end
    assert_equal JSON.parse(@response.body)['status'], STATUS_FAIL
  end

  # DELETE /lessons/1.json
  test 'should delete lesson by id' do
    assert_difference 'Lesson.count', -1 do
      delete :destroy, format: :json, id: Lesson.first.id
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

end
