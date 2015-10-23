require 'test_helper'

class SemestersControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE

  # GET /semesters.json
  test 'should get all semesters' do
    get :index, format: :json

    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['semesters']
    assert json['semesters'].count > 0
    assert_not_nil json['semesters'][0]['id']
    assert_not_nil json['semesters'][0]['name']
  end

  # POST /semesters.json
  test 'should add semester' do
    admin = User.find_by_name('alex')
    sign_in admin

    assert_difference 'Semester.count' do
      post :create, format: :json, semester: { name: 'a good semester' }
    end

    assert_json_success
    assert_not_nil json['id']
  end

  # POST /semesters.json
  test 'should not add semester if not admin' do
    user = User.last
    sign_in user
    assert_no_difference 'Semester.count' do
      post :create, format: :json, semester: { name: '123' }
    end
    assert_json_status STATUS_FAIL
    assert_json_reason REASON_PERMISSION_DENIED
  end

  # PUT /semesters/1.json
  test 'should update semester' do
    admin = User.find_by_name('alex')
    sign_in admin

    put :update, format: :json, id: Semester.first.id, semester: { name: 'update semester' }
    assert_equal 'update semester', Semester.first.name

    assert_json_success
  end

  # PUT /semesters/1.json
  test 'should not update semester if not admin' do
    user = User.last
    sign_in user

    put :update, format: :json, id: Semester.first.id, semester: { name: 'update semester' }

    assert_json_status STATUS_FAIL
    assert_json_reason REASON_PERMISSION_DENIED
  end

  # DELETE /semesters/1.json
  test 'should delete semester' do
    admin = User.find_by_name('alex')
    sign_in admin

    assert_difference 'Semester.count', -1 do
      delete :destroy, format: :json, id: Semester.first.id
    end

    assert_json_success
  end

  # DELETE /semesters/1.json
  test 'should not delete semester if not admin' do
    user = User.last
    sign_in user

    assert_no_difference 'Semester.count' do
      delete :destroy, format: :json, id: Semester.first.id
    end

    assert_json_status STATUS_FAIL
    assert_json_reason REASON_PERMISSION_DENIED
  end

  # GET /semesters/default.json
  test 'should get default semester' do
    get :default, format: :json

    assert_json_success
    assert_not_nil json['semester']
    assert_not_nil json['semester']['id']
    assert_not_nil json['semester']['name']
  end

end
