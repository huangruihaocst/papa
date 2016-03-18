require 'test_helper'

class AssistantsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  test 'api should get assistants by course' do
    teacher = User.find_by_name('alex')
    sign_in teacher

    get :index, { format: :json, course_id: Course.find_by_name('Operation System').id }
    
    assert_json_success
    assert json['assistants'].is_a? Array
  end

  test 'api should get one assistant' do
    teacher = User.find_by_name('alex')
    sign_in teacher

    get :show, { format: :json, id: User.first }

    assert_json_success
    assert_not_nil json['assistant']
  end

  test 'api should relate one assistant to a course' do
    teacher = User.find_by_name('alex')
    sign_in teacher

    assistant = User.find_by_name('betty')
    course = Course.first

    post :create, { format: :json, id: assistant.id, course_id: course.id }

    assert_json_success
  end

  test 'api should create many assistants to a course' do
    teacher = User.find_by_name('alex')
    sign_in teacher

    course = Course.first

    assistants = [
        {
            name: 'liming',
            phone: '12345666',
            email: 'alex@bilibili.com'
        },
        {
            name: 'liming1',
            phone: '123456661',
            email: 'alex@bilibili.com1'
        }
    ].to_json

    post :create_many, { format: :json, json: assistants, course_id: course.id }

    assert_json_success
  end

end
