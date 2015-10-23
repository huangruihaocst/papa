require 'test_helper'

class MessagesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  ## DONE

  # GET /students/1/messages.json
  test 'should get message by student' do
    student = User.find_by_name('alex')
    token = student.create_token.token

    get :index, format: :json, student_id: student.id, token: token
    assert_json_success

    assert_not_nil json['messages']
    assert json['messages'].count > 0
  end

  # GET /students/1/messages.json
  test 'should not get message if token is invalid' do
    student = User.find_by_name('alex')

    get :index, format: :json, student_id: student.id, token: 123

    assert STATUS_FAIL, json['status']
  end

  # GET /students/1/messages.json
  test 'should not get message if token is not matched' do
    student = User.find_by_name('alex')
    token = User.find_by_name('betty').create_token.token

    get :index, format: :json, student_id: student.id, token: token

    assert STATUS_FAIL, json['status']
  end

  # POST /courses/1/messages.json
  test 'should add message by teacher' do
    teacher = User.find_by_name('alex')
    course = Course.first
    sign_in teacher

    assert_difference 'Message.count' do
      post :create, format: :json, course_id: course.id, message: {
               type: 'homework',
               title: 'good news',
               deadline: Time.now + 24.hours,
               content: 'hahaha'
                  }
    end

    assert_json_success
  end

  test 'should not add message if parameters are invalid' do
    teacher = User.find_by_name('alex')
    course = Course.first
    sign_in teacher

    # wrong message_type
    assert_no_difference 'Message.count' do
      post :create, format: :json, course_id: course.id, message: {
                      type: '123',
                      title: 'good news',
                      deadline: Time.now + 24.hours,
                      content: 'hahaha'
                  }
    end
    assert STATUS_FAIL, json['status']

    # absence of title
    assert_no_difference 'Message.count' do
      post :create, format: :json, course_id: course.id, message: {
                      type: 'homework',
                      deadline: Time.now + 24.hours,
                      content: 'hahaha'
                  }
    end
    assert STATUS_FAIL, json['status']
  end

  # POST /courses/1/messages.json
  test 'should not add message if he is not a teacher' do
    non_teacher = User.find_by_name('betty')
    course = Course.first
    sign_in non_teacher

    # course_id invalid
    assert_no_difference 'Message.count' do
      post :create, format: :json, course_id: course.id, message: {
                      type: 'homework',
                      title: 'good news',
                      deadline: Time.now + 24.hours,
                      content: 'hahaha'
                  }
    end
    assert STATUS_FAIL, json['status']
  end

  # POST /courses/1/messages.json
  test 'should not add message if course_id is invalid' do
    teacher = User.find_by_name('alex')
    sign_in teacher

    # course_id invalid
    assert_no_difference 'Message.count' do
      post :create, format: :json, course_id: -1, message: {
                      type: 'homework',
                      title: 'good news',
                      deadline: Time.now + 24.hours,
                      content: 'hahaha'
                  }
    end
    assert STATUS_FAIL, json['status']
  end

end
