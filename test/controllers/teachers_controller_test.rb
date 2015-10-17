require 'test_helper'

class TeachersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  # GET /courses/1/teachers.json
  test 'should get teachers by course' do
    get :index, format: :json, course_id: Course.find_by_name('Operation System').id

    assert_json_success
    assert_not_nil json['teachers']
    assert json['teachers'].count > 0
  end

  # POST /courses/1/teachers/1.json
  test 'should add teacher to course' do
    admin = User.find_by_name('alex')
    sign_in admin

    teacher = User.find_by_name('ciara')
    course = Course.find_by_name('Operation System')

    assert_difference 'course.teachers.count' do
      post :create, id: teacher.id, course_id: course.id
    end

    assert_json_success
  end

  # POST /courses/1/teachers/1.json
  test 'should not add teacher if he is not an administrator' do
    non_admin = User.find_by_name('betty')
    sign_in non_admin

    teacher = User.find_by_name('ciara')
    course = Course.find_by_name('Operation System')

    assert_no_difference 'course.teachers.count' do
      post :create, id: teacher.id, course_id: course.id
    end

    assert STATUS_FAIL, json['status']
  end

  # POST /courses/1/teacher/1.json
  test 'should not add teacher if teacher_id or course_id is invalid' do
    admin = User.find_by_name('alex')
    sign_in admin

    teacher = User.find_by_name('ciara')
    course = Course.find_by_name('Operation System')

    assert_no_difference 'course.teachers.count' do
      post :create, id: teacher.id, course_id: -1
    end
    assert STATUS_FAIL, json['status']

    assert_no_difference 'course.teachers.count' do
      post :create, id: -1, course_id: course.id
    end
    assert STATUS_FAIL, json['status']
  end

  # DELETE /courses/1/teachers/1.json
  test 'should remove teacher from course' do
    admin = User.find_by_name('alex')
    sign_in admin

    course = Course.find_by_name('Operation System')
    teacher = course.teachers.first
    assert_difference 'course.teachers.count', -1 do
      delete :destroy, format: :json, id: teacher.id, course_id: course.id
    end

    assert_json_success
  end

  # DELETE /courses/1/teachers/1.json
  test 'should not remove teacher if teacher_id is invalid' do
    admin = User.find_by_name('alex')
    sign_in admin

    course = Course.find_by_name('Operation System')
    teacher = course.teachers.first

    assert_no_difference 'course.teachers.count' do
      delete :destroy, format: :json, id: teacher.id, course_id: -1
    end
    assert STATUS_FAIL, json['status']

    assert_no_difference 'course.teachers.count' do
      delete :destroy, format: :json, id: -1, course_id: course.id
    end
    assert STATUS_FAIL, json['status']
  end

  # DELETE /courses/1/teachers/1.json
  test 'should not remove teacher from course if he is not an administrator' do
    admin = User.find_by_name('betty')
    sign_in admin

    course = Course.find_by_name('Operation System')
    teacher = course.teachers.first
    assert_no_difference 'course.teachers.count' do
      delete :destroy, format: :json, id: teacher.id, course_id: course.id
    end

    assert STATUS_FAIL, json['status']
  end

end
