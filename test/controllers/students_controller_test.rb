require 'test_helper'

class StudentsControllerTest < ActionController::TestCase
  test 'api should get students by course' do
    get :index, { format: :json, course_id: Course.find_by_name('Operation System').id }

    assert_json_success
    assert json['students'].is_a? Array
  end

  test 'api should add student by course' do
    course = Course.first
    student = User.last

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
