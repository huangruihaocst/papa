require 'test_helper'

class AssistantsControllerTest < ActionController::TestCase
  test 'api should get assistants by course' do
    get :index, { format: :json, course_id: Course.find_by_name('Operation System').id }
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert json['assistants'].is_a? Array
  end
end
