require 'test_helper'

class LessonCommentsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  test 'should get comments by lesson id' do
    get :index, format: :json, lesson_id: Lesson.first.id
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['lesson_comments']
    assert json['lesson_comments'].count > 0
  end

  test 'should add comment to lesson by student' do
    student = User.find_by_name('betty')
    sign_in student

    assert_difference 'LessonComment.count' do
      post :create, format: :json, lesson_id: Lesson.first.id, lesson_comment: {
               content: 'bad',
               score: '5'
                  }
    end

    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

end
