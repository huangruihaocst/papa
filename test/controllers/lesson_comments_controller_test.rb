require 'test_helper'

class LessonCommentsControllerTest < ActionController::TestCase
  test 'should get comments by lesson id' do
    get :index, format: :json, lesson_id: Lesson.first.id
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['lesson_comments']
    assert json['lesson_comments'].count > 0
  end

end
