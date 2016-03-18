require 'test_helper'

class LessonCommentsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE

  # GET /lessons/1/comments.json
  test 'should get comments by lesson id' do
    get :index, format: :json, lesson_id: Lesson.first.id

    assert_json_success
    assert_not_nil json['lesson_comments']
    assert json['lesson_comments'].count > 0
  end

  # GET /lessons/1/comments.json
  test 'should not get comments by invalid lesson id' do
    get :index, format: :json, lesson_id: -1

    assert_json_status STATUS_FAIL
    assert_json_reason REASON_RESOURCE_NOT_FOUND
  end

  # POST /lessons/1/comments.json
  test 'should add comment to lesson by student from the course' do
    course = Course.find_by_name('Operation System')
    student = course.students.first
    lesson = course.lessons.first
    sign_in student

    assert_difference 'LessonComment.count' do
      post :create, format: :json, lesson_id: lesson.id, lesson_comment: {
               content: 'bad',
               score: '5'
                  }
    end

    assert_json_success
    assert_not_nil json['id']
  end

  # POST /lessons/1/comments.json
  test 'should not add comment to lesson by student from another course' do
    course = Course.last
    student = User.first
    lesson = course.lessons.first
    sign_in student

    assert_no_difference 'LessonComment.count' do
      post :create, format: :json, lesson_id: lesson.id, lesson_comment: {
                      content: 'bad',
                      score: '5'
                  }
    end

    assert_json_status STATUS_FAIL
    assert_json_reason REASON_PERMISSION_DENIED
  end

  # GET /students/1/lessons/1/comment.json
  test 'should get lesson comment by student and lesson' do
    course = Course.find_by_name('Operation System')
    student = course.students.first
    lesson = course.lessons.first
    sign_in student

    get :default, format: :json, student_id: student.id, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['lesson_comment']
  end

end
