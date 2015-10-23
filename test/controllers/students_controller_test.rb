require 'test_helper'

class StudentsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE but student 'sign in'

  # GET /courses/1/students.json
  test 'should get students by course' do
    course = Course.find_by_name('Operation System')
    sign_in course.teachers.first

    get :index, { format: :json, course_id: course.id }

    assert_json_success
    assert json['students'].is_a? Array
  end

  # GET /courses/1/students.json
  test 'should not get students by invalid course id' do
    sign_in Course.first.teachers.first
    get :index, { format: :json, course_id: -1 }

    assert_equal STATUS_FAIL, json['status']
    assert_equal REASON_RESOURCE_NOT_FOUND, json['reason']
  end

  # POST /courses/1/students/1.json
  test 'should add student by course' do
    course = Course.first
    teacher = course.teachers.first
    student = User.last
    sign_in teacher

    assert_difference 'Participation.count' do
      post :create, format: :json, course_id: course.id, id: student.id
    end

    assert_json_success
  end

  # DELETE /courses/1/students/1.json
  test 'should remove student from course' do
    course = Participation.first.course
    teacher = course.teachers.first
    student = Participation.first.user

    sign_in teacher

    assert_difference 'Participation.count', -1 do
      delete :destroy, format: :json, course_id: course.id, id: student.id
    end

    assert_json_success
  end

  # DELETE /courses/1/students/1.json
  test 'should not remove student from course if not a teacher of the course' do
    course = Participation.first.course
    teacher = User.find_by_name('betty')
    student = Participation.first.user

    sign_in teacher

    assert_no_difference 'Participation.count' do
      delete :destroy, format: :json, course_id: course.id, id: student.id
    end
    assert_equal STATUS_FAIL, json['status']
  end

end
