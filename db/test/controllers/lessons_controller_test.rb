require 'test_helper'

class LessonsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  # DONE

  def setup
    @course = Course.find_by_name('Operation System')
    @teacher = @course.teachers.first
  end

  # GET /course/1/lessons.json
  test 'api should get all lessons by course' do
    get :index, format: :json, course_id: @course.id

    assert_equal json['status'], STATUS_SUCCESS
    assert json['lessons'].count > 0
    assert_not_nil json['lessons'][0]['name']
    assert_not_nil json['lessons'][0]['start_time']
    assert_not_nil json['lessons'][0]['end_time']
    assert_not_nil json['lessons'][0]['location']
  end

  # GET /lessons/1.json
  test 'api should get lesson by id' do
    get :show, format: :json, id: Lesson.first.id

    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['lesson']['name']
    assert_not_nil json['lesson']['start_time']
    assert_not_nil json['lesson']['end_time']
    assert_not_nil json['lesson']['location']
  end

  # POST /course/1/lessons.json
  test 'api should add lesson to course' do
    sign_in @teacher
    assert_difference('Lesson.count') do
      post :create, format: :json, course_id: @course.id, lesson: {
                      description: '123',
                      name: 'test lesson',
                      start_time: '2015/10/10 10:20:30',
                      end_time: '2015/10/10 10:20:30',
                      location: '"name": "here"'
                  }
    end
    assert_json_success
    assert_not_nil json['id']
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
    teacher = @course.teachers.first
    lesson = @course.lessons.first
    sign_in teacher

    assert_difference 'Lesson.count', -1 do
      delete :destroy, format: :json, id: lesson.id
    end

    assert_json_success
  end

  # DELETE /lessons/1.json
  test 'should not delete lesson by id if he is not a teacher of the lesson' do
    teacher = User.find_by_name('betty')
    lesson = @course.lessons.first
    sign_in teacher

    assert_difference 'Lesson.count', -1 do
      delete :destroy, format: :json, id: lesson.id
    end

    assert_json_success
  end

end
