require 'test_helper'

class FilesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  # GET /students/1/lessons/1/files.json
  test 'should get student files on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')

    sign_in student
    get :index, format: :json, student_id: student.id, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['files']
    assert json['files'].count > 0
  end

  # GET /assistants/1/lessons/1/files.json
  test 'should get assistant files on lesson' do
    assistant = User.find_by_name('alex')
    lesson = Lesson.find_by_name('exp1')

    sign_in assistant
    get :index, format: :json, assistant_id: assistant.id, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['files']
    assert json['files'].count > 0
  end

  # GET /lessons/1/files.json
  test 'should get lesson files' do
    lesson = Lesson.find_by_name('exp1')

    get :index, format: :json, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['files']
    assert json['files'].count > 0
  end

  # GET /courses/1/files.json
  test 'should get course files' do
    course = Course.find_by_name('Operation System')

    get :index, format: :json, course_id: course.id

    assert_json_success
    assert_not_nil json['files']
    assert json['files'].count > 0
  end

  # GET /files/1.json
  test 'should get file by id' do
    file = FileResource.first
    get :show, format: :json, id: file.id

    assert_json_success
    assert_not_nil json['file']
    assert_not_nil json['file']['id']
    assert_not_nil json['file']['name']
    assert_not_nil json['file']['path']
    assert_not_nil json['file']['created_at']
    assert_not_nil json['file']['type']
  end

  # POST /files.json
  test 'should add file' do
    sign_in User.first

    fixture_file = fixture_file_upload('files/2.jpg', 'image/jpeg')

    assert_difference 'FileResource.count' do
      post :create, format: :json, file: {file: fixture_file, type: 'picture'}
    end

    assert_json_success
    assert_not_nil assigns(:file).path
    assert json['id']
  end

  # POST /files.json
  test 'should not add file if type and file not set' do
    sign_in User.first

    assert_no_difference 'FileResource.count' do
      post :create, format: :json, file: {}
    end

    assert_equal STATUS_FAIL, json['status']
    assert_not_nil REASON_INVALID_FIELD, json['reason']
    assert json[INVALID_FIELDS_NAME].include? 'type'
    assert json[INVALID_FIELDS_NAME].include? 'file'
    assert_not json[INVALID_FIELDS_NAME].include? 'path'
    assert_not json[INVALID_FIELDS_NAME].include? 'name'
  end

  # POST /files.json
  test 'should not add file if not signed in' do
    assert_no_difference 'FileResource.count' do
      post :create, format: :json, file: {}
    end
    assert_equal STATUS_FAIL, json['status']
    assert_not_nil REASON_TOKEN_INVALID, json['reason']
  end

  # POST /files.json
  test 'should not add file if the file is too big' do
    sign_in User.first

    file_name = '__test_big_file'
    file_path = File.join(Rails.root, 'test', 'fixtures', 'files', file_name)
    big_file = File.new(file_path, 'wb+')

    # 160M
    x = '012345678901234567890'
    23.times do
      x += x
    end

    big_file.puts x
    big_file.close

    fixture_file = fixture_file_upload("files/#{file_name}", 'image/jpeg')

    assert_no_difference 'FileResource.count' do
      post :create, format: :json, file: {file: fixture_file, type: 'picture'}
    end

    assert_equal STATUS_FAIL, json['status']
    assert_equal REASON_FILE_TOO_BIG, json['reason']

    File.delete(file_path)
  end

  # POST /courses/1/files.json

  # DELETE /files/1.json
  test 'should delete file if he is the creator' do
    sign_in User.first
    fixture_file = fixture_file_upload('files/2.jpg', 'image/jpeg')
    post :create, format: :json, file: {file: fixture_file, type: 'picture'}
    id = json['id']

    assert_difference 'FileResource.count', -1 do
      delete :destroy, format: :json, id: id.to_i
    end

    assert_json_success
  end

end
