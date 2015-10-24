require 'test_helper'

class StudentCommentsControllerTest < ActionController::TestCase

  include Devise::TestHelpers

  test 'should get student comment on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')

    get :index, format: :json, student_id: student.id, lesson_id: lesson.id

    assert_json_success
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

    assert_json_success
  end

end
