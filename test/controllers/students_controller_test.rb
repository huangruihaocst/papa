require 'test_helper'

class StudentsControllerTest < ActionController::TestCase
  test 'api should get students by course' do
    get :index, { format: :json, course_id: Course.find_by_name('Operation System').id }
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['students'].is_a? Array
  end

  test 'api should add student by course' do

  end

  test 'api should remove student from course' do

  end

end
