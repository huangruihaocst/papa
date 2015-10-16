require 'test_helper'

class MessagesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  test 'should get message by students' do
    student = User.find_by_name('alex')
    get :index, format: :json, student_id: student.id

    assert_json_success
    assert_not_nil json['messages']
    assert json['messages'].count > 0
  end

  test 'should add message by teacher' do
    teacher = User.find_by_name('alex')
    course = Course.first
    sign_in teacher

    assert_difference 'Message.count' do
      post :create, format: :json, course_id: course.id, message: {
               message_type: 'homework',
               title: 'good news',
               deadline: Time.now + 24.hours,
               content: 'hahaha'
                  }
    end

    assert_json_success
  end

end
