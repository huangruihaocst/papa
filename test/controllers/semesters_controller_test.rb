require 'test_helper'

class SemestersControllerTest < ActionController::TestCase

  # GET /semesters.json
  test 'should get all semesters' do
    get :index, format: :json

    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['semesters']
    assert json['semesters'].count > 0
  end

  # POST /semesters.json
  test 'should add semester' do
    assert_difference 'Semester.count' do
      post :create, format: :json, semester: { name: 'a good semester' }
    end

    assert_json_success
  end

  # PUT /semesters/1.json
  test 'should update semester' do
    put :update, format: :json, id: Semester.first.id, semester: { name: 'update semester' }
    assert_equal 'update semester', Semester.first.name

    assert_json_success
  end

  # DELETE /semesters/1.json
  test 'should delete semester' do
    assert_difference 'Semester.count', -1 do
      delete :destroy, format: :json, id: Semester.first.id
    end

    assert_json_success
  end

  # GET /semesters/default.json
  test 'should get default semester' do
    get :default, format: :json

    assert_json_success
    assert_not_nil json['semester']
  end

end
