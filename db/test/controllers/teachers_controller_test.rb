require 'test_helper'

class TeachersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE except update

  # GET /courses/1/teachers.json
  test 'should get teachers by course' do
    get :index, format: :json, course_id: Course.find_by_name('Operation System').id

    assert_json_success
    assert_not_nil json['teachers']
    assert json['teachers'].count > 0
  end

  # GET /courses/1/teachers.json
  test 'should not get teachers by wrong course' do
    get :index, format: :json, course_id: -1

    assert_json_status STATUS_FAIL
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

  # DELETE /teachers.1.json
  test 'should delete teacher' do
    admin = User.find_by_name('alex')
    sign_in admin
    teacher = User.find_by_name('deng')
    assert_difference 'User.count', -1 do
      delete :destroy, format: :json, id: teacher.id
    end
    assert_json_success
  end

  # DELETE /teachers.1.json
  test 'should not delete teacher with invalid id' do
    admin = User.find_by_name('alex')
    sign_in admin
    assert_difference 'User.count', 0 do
      delete :destroy, format: :json, id: -1
    end
    assert_json_status STATUS_FAIL
  end

  # GET /teachers/1.json
  test 'should get teacher' do
    teacher = User.find_by_name('alex')
    get :show, format: :json, id: teacher.id
    assert_json_success
  end

  # GET /teachers/1.json
  test 'should not get invalid teacher' do
    get :show, format: :json, id: -1
    assert_json_status STATUS_FAIL
  end

  # GET /teachers/1.json
  test 'should get wrong teacher' do
    teacher = User.find_by_name('betty')
    get :show, format: :json, id: teacher.id
    assert_json_status STATUS_FAIL
  end

  # POST /lessons/1/start_sign_up.json
  test 'should start sign up as a teacher' do
    teacher = User.find_by_name('alex')
    sign_in teacher
    lesson = Course.find_by_name('Operation System').lessons.first
    get :start_sign_up, format: :json, lesson_id: lesson.id
    assert_json_success
  end

end
