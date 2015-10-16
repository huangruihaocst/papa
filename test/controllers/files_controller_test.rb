require 'test_helper'

class FilesControllerTest < ActionController::TestCase

  # GET /students/1/lessons/1/files.json
  test 'should get student files on lesson' do
    student = User.find_by_name('betty')
    lesson = Lesson.find_by_name('exp1')

    get :index, format: :json, student_id: student.id, lesson_id: lesson.id

    assert_json_success
    assert_not_nil json['files']
    assert json['files'].count > 0
  end

  # GET /assistants/1/lessons/1/files.json
  test 'should get assistant files on lesson' do
    assistant = User.find_by_name('alex')
    lesson = Lesson.find_by_name('exp1')

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
  end

  # POST /files.json
  test 'should add file' do
    fixture_file = fixture_file_upload('files/2.jpg', 'image/jpeg')

    assert_difference 'FileResource.count' do
      post :create, format: :json, file: {file: fixture_file, type: 'picture'}
    end

    assert_json_success
    assert_not_nil assigns(:file).path
  end

end
