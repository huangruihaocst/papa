require 'test_helper'

class StudentCommentsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  # GET /lessons/1/students/1/comments.json
  test 'should get comments by lesson and student' do
    student = User.find_by_name('betty')
    get :index, format: :json, student_id: student.id, lesson_id: Lesson.first.id
    assert_json_success
  end

  # GET /lessons/1/students/1/comment.json
  test 'should get student comment on lesson' do
    lesson = Lesson.find_by_name('exp1')
    student = User.find_by_name('betty')

    get :default, format: :json, student_id: student.id, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['student_comment']
  end

  # POST /lessons/1/students/1/comments.json
  test 'should add student comment on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')
    creator = User.find_by_name('alex')
    sign_in creator

    assert_difference 'StudentComment.count' do
      post :create, format: :json, student_id: student.id, lesson_id: lesson.id, student_comment: {
               content: 'and',
               score: '123'
                  }
    end

    assert_json_success
    assert_not_nil json['id']
  end
end
