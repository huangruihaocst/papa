require 'test_helper'

class SemestersControllerTest < ActionController::TestCase
  test 'should get all semesters' do
    get :index, format: :json
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['semesters']
    assert json['semesters'].count > 0
  end

  test 'should add semester' do
    assert_difference 'Semester.count' do
      post :create, format: :json, semester: { name: 'a good semester' }
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  test 'should update semester' do
    put :update, format: :json, id: Semester.first.id, semester: { name: 'update semester' }
    assert_equal 'update semester', Semester.first.name
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  test 'should delete semester' do
    assert_difference 'Semester.count', -1 do
      delete :destroy, format: :json, id: Semester.first.id
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  test 'should get default semester' do
    get :default, format: :json
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert_not_nil json['semester']
  end

end
