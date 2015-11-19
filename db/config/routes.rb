Rails.application.routes.draw do

  # error handling
  get '/404' => 'errors#not_found'

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  get 'users/current' => 'users#current'
  get 'users/:id' => 'users#show'
  put 'users/:id' => 'users#update'

  resources :semesters, only: [:index, :create, :update, :destroy] do
    resources :courses, only: [:index]
  end
  get 'semesters/default' => 'semesters#default'

  resources :courses, only: [:show, :update, :destroy] do
    resources :students, only: [:index, :destroy]
    post 'students/:id' => 'students#create', as: :create_student
    post 'students' => 'students#create_many', as: :create_many_student

    resources :assistants, only: [:index, :create, :destroy]
    post 'assistants/:id' => 'assistants#create', as: :create_assistant

    resources :lessons, only: [:index, :create, :destroy]
    resources :teachers, only: [:index, :create, :destroy, :update, :show]
    post 'teachers/:id' => 'teachers#create', as: :create_teacher

    resources :messages, only: [:create]
    resources :files, only: [:index, :create, :delete]
    get 'comments' => 'lesson_comments#index', as: :comments_of_course
  end

  resources :lessons, only: [:show, :destroy, :update] do
    # students' comments to the lesson
    resources :comments, controller: 'lesson_comments', only: [:index, :create]
    post 'attendance' => 'attendance#create'
    delete 'attendance' => 'attendance#destroy'

    # for comment and score
    resources :students, only: [:show] do
      # assistants' comments to the student
      resources :comments, controller: 'student_comments', only: [:index, :create]
      get 'comment' => 'student_comments#default'

    end

    # for attendence
    resources :students, only: [:index, :create]
    post 'students/:id' => 'students#create'
    post 'start_sign_up' => 'teachers#start_sign_up'

    resources :files, only: [:index, :create, :delete]
  end

  resources :students, only: [:index, :show, :create, :update, :destroy] do
    resources :courses, only: [:index, :update, :destroy]
    post 'courses/:id' => 'courses#create', as: :create_course

    resources :files, only: [:index, :create, :destroy]
    resources :lessons, only: [:show, :update] do
      get 'comment' => 'lesson_comments#default'
      post 'comments' => 'lesson_comments#create'
      resources :files, only: [:index, :show, :create, :destroy]
    end
    resources :messages, only: [:index]
  end

  resources :assistants, only: [:index, :show, :create, :update, :destroy] do
    resources :courses, only: [:index, :create]
    post 'courses/:id' => 'courses#create'
    resources :files, only: [:index, :create]
  end

  resources :teachers, only: [:index, :show, :create, :update, :destroy] do
    resources :courses, only: [:index, :create, :update, :destroy]
    get 'lessons' => 'lessons#from_teacher'
  end

  resources :files, only: [:show, :create, :destroy]

  resources :messages, only: [:show]

  get 'messages' => 'user_messages#index', as: :user_messages
  get 'new_message_count' => 'user_messages#new_message_count', as: :new_message_count
  post 'users/:user_id/messages' => 'user_messages#create', as: :create_user_message
  post 'messages/:message_id/read' => 'user_messages#read', as: :read_message
  delete 'messages/:id' => 'user_messages#destroy'

  namespace :apps do
    get 'current_version' => 'android_apps#current_version'
    post 'current_version' => 'android_apps#create'
  end

  get 'test' => 'test#index'
  namespace :manage do
    get '' => 'manage#main_page'
    get 'MainPage' => 'manage#main_page' ,as: :main_page
    get 'CourseScore/:id' => 'manage#course_score'
    get 'ClassScore/:id' => 'manage#class_score'
    get 'CourseInfo/:id' => 'manage#course_info'
    get 'ClassInfo/:id' => 'manage#class_info'
    get 'ShowPhotos/:id' => 'manage#show_photos'
    get 'student/:id/lesson/:lessonId' => 'manage#student_lesson_info'
    get 'CourseStudents/:id' => 'manage#course_students'
    get 'Message' => 'manage#message'
  end
  root "manage/manage#main_page"
end
