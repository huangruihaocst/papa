require 'test_helper'

class StudentsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  test 'api should get students by course' do
    course = Course.find_by_name('Operation System')
    sign_in course.teachers.first

    get :index, { format: :json, course_id: course.id }

    assert_json_success
    assert json['students'].is_a? Array
  end

  test 'api should add student by course' do
    course = Course.first
    student = User.last
    sign_in student

    assert_difference 'Participation.count' do
      post :create, format: :json, course_id: course.id, id: student.id
    end

    assert_json_success
  end

  test 'api should remove student from course' do
    course = Participation.first.course
    student = Participation.first.user

    assert_difference 'Participation.count', -1 do
      delete :destroy, format: :json, course_id: course.id, id: student.id
    end

    assert_json_success
  end

end
