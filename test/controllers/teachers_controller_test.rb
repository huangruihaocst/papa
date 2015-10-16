require 'test_helper'

class TeachersControllerTest < ActionController::TestCase
  test 'should get teachers by course' do
    get :index, format: :json, course_id: Course.find_by_name('Operation System').id

    assert_json_success
    assert_not_nil json['teachers']
    assert json['teachers'].count > 0
  end

  test 'should add teacher to course' do
    teacher = User.find_by_name('ciara')
    course = Course.find_by_name('Operation System')
    course_creator = course.teachers.first
    token_str = course_creator.create_token.token

    assert_difference 'course.teachers.count' do
      post :create, id: teacher.id, course_id: course.id, token: token_str
    end

    assert_json_success
  end

  test 'should remove teacher from course' do
    course = Course.find_by_name('Operation System')
    teacher = course.teachers.first
    assert_difference 'course.teachers.count', -1 do
      delete :destroy, format: :json, id: teacher.id, course_id: course.id
    end

    assert_json_success
  end

end
