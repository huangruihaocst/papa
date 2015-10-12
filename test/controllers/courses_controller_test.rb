require 'test_helper'

class CoursesControllerTest < ActionController::TestCase

  # GET /semesters/1/courses.json
  test 'api should get courses by semester' do
    semester = Semester.first

    get :index, format: :json, semester_id: semester.id
    json = JSON.parse(@response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert json['courses'].count > 0
  end

  # GET /semester/1/courses.json
  test 'should not get courses by bad semester' do
    get :index, format: :json, semester_id: -1
    json = JSON.parse(response.body)
    assert_equal STATUS_FAIL, json['status']
  end

  # GET /teachers/1/courses.json
  test 'api should get courses by teacher' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    get :index, format: :json, teacher_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal STATUS_SUCCESS, json['status']
    assert json['courses'].count > 0
  end

  # GET /assistants/1/courses.json
  test 'api should get courses by assistant with token' do
    u = User.find_by_name('betty')
    token_str = u.create_token.token

    get :index, format: :json, assistant_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['courses']
  end

  # GET /students/1/courses.json
  test 'api should get courses by student with token' do
    u = User.first
    token_str = u.create_token.token

    get :index, format: :json, student_id: u.id, token: token_str
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['courses']
  end

  # GET /student/1/courses.json
  test 'api should not get courses by student with invalid token' do
    u = User.first

    get :index, format: :json, student_id: u.id, token: 'invalid token'
    json = JSON.parse(@response.body)
    assert_equal STATUS_FAIL, json['status']
    assert_equal json['reason'], REASON_TOKEN_INVALID
  end

  # GET /student/1/courses.json
  test 'api should not get courses by student if time out' do
    u = User.first
    # create an invalid token
    token = Token.create(user_id: u.id, token: rand(TOKEN_MAX_RAND).to_s, valid_until: Time.now - 200)

    get :index, format: :json, student_id: u.id, token: token.token
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_FAIL
    assert_equal json['reason'], REASON_TOKEN_TIMEOUT
  end

  # GET /courses/1.json
  test 'api should get course by id' do
    get :show, format: :json, id: Course.first.id
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_SUCCESS
    assert_not_nil json['course']['name']
  end

  # GET /courses/-1.json
  test 'api should not get course by invalid id' do
    get :show, format: :json, id: -1
    json = JSON.parse(@response.body)
    assert_equal json['status'], STATUS_FAIL
  end

  # PUT /courses/1.json
  test 'api should update course by id with teacher token' do
    course = Course.find_by_name('Operation System')
    course_id = course.id
    teacher = course.teachers.first
    token_str = teacher.create_token.token

    put :update, format: :json, id: course.id, token: token_str, course: { name: 'fuck' }
    assert_equal 'fuck', Course.find(course_id).name
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  # POST /teachers/1/courses.json
  test 'api should create course by teacher' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    assert_difference 'TeachingCourse.count' do
      post :create, teacher_id: u.id, token: token_str, course: { name: 'test course', description: 'new' }
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  # POST /assistants/1/courses/1.json
  test 'api should relate course to assitant' do
    u = User.find_by_name('ciara')
    token_str = u.create_token.token
    course = Course.first

    assert_difference 'Participation.count' do
      post :create, assistant_id: u.id, id: course.id, token: token_str
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  # POST /students/1/courses/1.json
  test 'api should relate course to student' do
    u = User.find_by_name('betty')
    token_str = u.create_token.token
    course = Course.first

    assert_difference 'Participation.count' do
      post :create, student_id: u.id, id: course.id, token: token_str
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end


  # DELETE /courses/1.json
  test 'api should delete course by id with teacher token' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    course = Course.find_by_name('Operation System')
    teacher = course.teachers.first
    token_str = teacher.create_token.token
    assert_difference 'Course.count', -1 do
      delete :destroy, format: :json, id: course.id, token: token_str
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  # DELETE /students/1/courses/1.json
  test 'should unrelate course with student' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    user = User.find_by_name('alex')
    course = Course.find_by_name('Operation System')

    assert_difference 'Participation.count', -1 do
      delete :destroy, foramt: :json, student_id: user.id, id: course.id, token: token_str
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end

  # DELETE /assistants/1/courses/1.json
  test 'should unrelate course with assistant' do
    u = User.find_by_name('alex')
    token_str = u.create_token.token

    p = Participation.where(role: ROLE_ASSISTANT).first
    user = p.user
    course = p.course

    assert_difference 'Participation.count', -1 do
      delete :destroy, foramt: :json, assistant_id: user.id, id: course.id, token: token_str
    end
    json = JSON.parse(response.body)
    assert_equal STATUS_SUCCESS, json['status']
  end
end
