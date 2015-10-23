require 'test_helper'

class RoutesTest < ActionController::TestCase

  # /users/xx
  test 'should generate user urls' do
    assert_routing('users/current.json',
                   { controller: 'users', action: 'current', format: 'json' })
    assert_routing({ method: :post, path: 'users/sign_in.json' },
                   { controller: 'users/sessions', action: 'create', format: 'json' })
    assert_routing({ method: :delete, path: 'users/sign_out.json'},
                   { controller: 'users/sessions', action: 'destroy', format: 'json' })
    assert_routing('users/1.json',
                   { controller: 'users', action: 'show', format: 'json', id: '1'})
  end

  # /semesters/xx
  test 'should generate semester urls' do
    assert_routing('/semesters.json',
                   { controller: 'semesters', action: 'index', format: 'json' })
    assert_routing({ method: :post, path: '/semesters.json' },
                   { controller: 'semesters', action: 'create', format: 'json'})
    assert_routing({ method: :put, path: '/semesters/1.json' },
                   { controller: 'semesters', action: 'update', format: 'json',id: '1' })
    assert_routing({ method: :delete, path: '/semesters/1.json' },
                   { controller: 'semesters', action: 'destroy', format: 'json', id: '1' })
    assert_routing('/semesters/1/courses.json',
                   controller: 'courses', action: 'index', format: 'json', semester_id: '1')
    assert_routing('/semesters/default.json',
                   controller: 'semesters', action: 'default', format: 'json')
  end

  # /courses/xx
  test 'should generate courses urls' do
    assert_routing({ method: 'put', path: '/courses/1.json' },
                   { controller: 'courses', action: 'update', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/courses/1.json' },
                   { controller: 'courses', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'delete', path: '/courses/1.json' },
                   { controller: 'courses', action: 'destroy', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/courses/1/teachers.json' },
                   { controller: 'teachers', action: 'index', format: 'json', course_id: '1' })
    assert_routing({ method: 'post', path: '/courses/1/teachers/2.json'},
                   { controller: 'teachers', action: 'create', format: 'json', course_id: '1', id: '2'})
    assert_routing({ method: 'delete', path: '/courses/1/teachers/2.json' },
                   { controller: 'teachers', action: 'destroy', format: 'json', course_id: '1', id: '2'})
    assert_routing({ method: 'get', path: '/courses/1/students.json'},
                   { controller: 'students', action: 'index', format: 'json', course_id: '1'})
    assert_routing({ method: 'get', path: '/courses/1/assistants.json'},
                   { controller: 'assistants', action: 'index', format: 'json', course_id: '1'})
    assert_routing({ method: 'post', path: '/courses/1/students/2.json'},
                   { controller: 'students', action: 'create', format: 'json', course_id: '1', id: '2'})
    assert_routing({ method: 'post', path: '/courses/1/assistants/2.json'},
                   { controller: 'assistants', action: 'create', format: 'json', course_id: '1', id: '2'})
    assert_routing({ method: 'get', path: '/courses/1/lessons.json'},
                   { controller: 'lessons', action: 'index', format: 'json', course_id: '1'})
    assert_routing({ method: 'post', path: '/courses/1/lessons.json'},
                   { controller: 'lessons', action: 'create', format: 'json', course_id: '1'})
    assert_routing({ method: 'delete', path: '/lessons/1.json' },
                   { controller: 'lessons', action: 'destroy', format: 'json', id: '1' })
  end

  # /lessons/xx
  test 'should generate lesson urls' do
    assert_routing({ method: 'get', path: '/lessons/1.json' },
                   { controller: 'lessons', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/lessons/1/comments.json' },
                   { controller: 'lesson_comments', action: 'index', format: 'json', lesson_id: '1' })
    assert_routing({ method: 'post', path: '/lessons/1/comments.json' },
                   { controller: 'lesson_comments', action: 'create', format: 'json', lesson_id: '1' })
    assert_routing({ method: 'get', path: '/lessons/1/students/2/comments.json' },
                   { controller: 'student_comments', action: 'index', format: 'json', lesson_id: '1', student_id: '2' })
    assert_routing({ method: 'post', path: '/lessons/1/students/2/comments.json' },
                   { controller: 'student_comments', action: 'create', format: 'json', lesson_id: '1', student_id: '2' })
    assert_routing({ method: 'post', path: '/lessons/1/comments.json' },
                   { controller: 'lesson_comments', action: 'create', format: 'json', lesson_id: '1' })
    assert_routing({ method: 'get', path: '/students/1/lessons/2/comments.json'},
                   { controller: 'lesson_comments', action: 'from_student', format: 'json', student_id: '1', lesson_id: '2' })
    assert_routing({ method: 'get', path: '/lessons/1/students.json' },
                   { controller: 'students', action: 'index', format: 'json', lesson_id: '1' })
    assert_routing({ method: 'post', path: '/lessons/1/students/2.json' },
                   { controller: 'students', action: 'create', format: 'json', lesson_id: '1', id: '2' })
    assert_routing({ method: 'get', path: '/lessons/1/files.json' },
                   { controller: 'files', action: 'index', format: 'json', lesson_id: '1' })
    assert_routing({ method: 'post', path: '/lessons/1/files.json' },
                   { controller: 'files', action: 'create', format: 'json', lesson_id: '1' })
  end

  # /students/xx
  test 'should generate students urls' do
    assert_routing({ method: 'get', path: '/students/1.json' },
                   { controller: 'students', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/students/1/files.json' },
                   { controller: 'files', action: 'index', format: 'json', student_id: '1' })
    assert_routing({ method: 'post', path: '/students/1/files.json' },
                   { controller: 'files', action: 'create', format: 'json', student_id: '1' })
    assert_routing({ method: 'delete', path: '/students/1/files/2.json' },
                   { controller: 'files', action: 'destroy', format: 'json', student_id: '1', id: '2' })

    assert_routing({ method: 'get', path: '/students/1/courses.json' },
                   { controller: 'courses', action: 'index', format: 'json', student_id: '1' })
    assert_routing({ method: 'post', path: '/students/1/courses/2.json' },
                   { controller: 'courses', action: 'create', format: 'json', student_id: '1', id: '2' })
    assert_routing({ method: 'delete', path: '/students/1/courses/2.json' },
                   { controller: 'courses', action: 'destroy', format: 'json', student_id: '1', id: '2' })
    assert_routing({ method: 'get', path: '/students/1/lessons/2/files.json' },
                   { controller: 'files', action: 'index', format: 'json', student_id: '1', lesson_id: '2'})
    assert_routing({ method: 'post', path: '/students/1/lessons/2/files.json' },
                   { controller: 'files', action: 'create', format: 'json', student_id: '1', lesson_id: '2'})
    assert_routing({ method: 'delete', path: '/students/1/lessons/2/files/3.json' },
                   { controller: 'files', action: 'destroy', format: 'json', student_id: '1', lesson_id: '2', id: '3' })
  end

  # /assistants/xx
  test 'should generate assistant urls' do
    assert_routing({ method: 'get', path: '/assistants.json' },
                   { controller: 'assistants', action: 'index', format: 'json' })
    assert_routing({ method: 'get', path: '/assistants/1.json' },
                   { controller: 'assistants', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/assistants/1/courses.json' },
                   { controller: 'courses', action: 'index', format: 'json', assistant_id: '1' })
    assert_routing({ method: 'post', path: '/assistants/1/courses/2.json' },
                   { controller: 'courses', action: 'create', format: 'json', assistant_id: '1', id: '2' })
    assert_routing({ method: 'get', path: '/assistants/1/files.json' },
                   { controller: 'files', action: 'index', format: 'json', assistant_id: '1' })
  end

  # /teachers/xx
  test 'should generate teacher urls' do
    assert_routing({ method: 'get', path: '/teachers/1.json' },
                   { controller: 'teachers', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'post', path: '/teachers.json' },
                   { controller: 'teachers', action: 'create', format: 'json' })
    assert_routing({ method: 'delete', path: '/teachers/1.json' },
                   { controller: 'teachers', action: 'destroy', format: 'json', id: '1' })
    assert_routing({ method: 'put', path: '/teachers/1.json' },
                   { controller: 'teachers', action: 'update', format: 'json', id: '1' })
    assert_routing({ method: 'get', path: '/teachers/1/courses.json' },
                   { controller: 'courses', action: 'index', format: 'json', teacher_id: '1' })
    assert_routing({ method: 'post', path: '/teachers/1/courses.json' },
                   { controller: 'courses', action: 'create', format: 'json', teacher_id: '1' })
  end

  # about messages
  test 'should generate message urls' do
    assert_routing({ method: 'get', path: '/students/1/messages.json' },
                   { controller: 'messages', action: 'index', format: 'json', student_id: '1' })
    assert_routing({ method: 'post', path: '/courses/1/messages.json' },
                   { controller: 'messages', action: 'create', format: 'json', course_id: '1' })
  end

  # /files/xx
  test 'should generate file urls' do
    assert_routing({ method: 'get', path: '/files/1.json' },
                   { controller: 'files', action: 'show', format: 'json', id: '1' })
    assert_routing({ method: 'post', path: '/files.json' },
                   { controller: 'files', action: 'create', format: 'json' })
    assert_routing({ method: 'delete', path: '/files/1.json' },
                   { controller: 'files', action: 'destroy', format: 'json', id: '1' })
  end

  # /android/
  test 'should generate android urls' do
    assert_routing({ method: 'get', path: '/apps/current_version.json' },
                   { controller: 'apps/android_apps', action: 'current_version', format: 'json' })
    assert_routing({ method: 'post', path: '/apps/current_version.json' },
                   { controller: 'apps/android_apps', action: 'create', format: 'json' })
  end


end