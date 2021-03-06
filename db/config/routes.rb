Rails.application.routes.draw do

  # error handling
  get "/404" => "errors#not_found"

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  get 'users/current' => 'users#current'
  get 'users/:id' => 'users#show'

  resources :semesters, only: [:index, :create, :update, :destroy] do
    resources :courses, only: [:index]
  end
  get 'semesters/default' => 'semesters#default'

  resources :courses, only: [:show, :update, :destroy] do
    resources :students, only: [:index, :destroy]
    post 'students/:id' => 'students#create', as: :create_student

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

    # for comment and score
    resources :students, only: [:show] do
      # assistants' comments to the student
      resources :comments, controller: 'student_comments', only: [:index, :create]
      get 'comment' => 'student_comments#default'
    end

    # for attendence
    resources :students, only: [:index, :create]
    post 'students/:id' => 'students#create'

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
  end

  resources :files, only: [:show, :create, :destroy]

  namespace :apps do
    get 'current_version' => 'android_apps#current_version'
    post 'current_version' => 'android_apps#create'
  end

  get 'test' => 'test#index'
  get 'manage' => 'manage#main_page'
  get 'manage/MainPage' => 'manage#main_page'
  get 'manage/CourseScore/:id' => 'manage#course_score'
  get 'manage/ClassScore/:id' => 'manage#class_score'
  get 'manage/CourseInfo/:id' => 'manage#course_info'
  get 'manage/ClassInfo/:id' => 'manage#class_info'
  get 'manage/ShowPhotos/:id' => 'manage#show_photos'
  get 'manage/student/:id/lesson/:lessonId' => 'manage#student_lesson_info'
  post 'manage/AddCourseToCurrentUser' => 'manage#AddCourseToCurrentUser'
end
