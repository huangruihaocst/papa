require 'test_helper'

class StudentCommentsControllerTest < ActionController::TestCase

  include Devise::TestHelpers

  test 'should get student comment on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')

    get :index, format: :json, student_id: student.id, lesson_id: lesson.id
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['student_comments']
    assert json['student_comments'].count
  end

  test 'should add student comment on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')
    creator = User.find_by_name('alex')
    sign_in creator

    assert_difference 'StudentComment.count' do
      post :create, format: :json, student_id: student.id, lesson_id: lesson.id, student_comment: {
               content: 'haha',
               score: '123'
                  }
    end

    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

end
