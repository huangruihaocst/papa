Rails.application.routes.draw do

  # error handling
  get "/404" => "errors#not_found"

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  get 'users/current_user.json' => 'users#current'

  resources :semesters, only: [:index, :create, :update, :destroy] do
    resources :courses, only: [:index]
  end
  get 'semesters/default' => 'semesters#default'

  resources :courses, only: [:show, :update, :destroy] do
    resources :students, only: [:index, :destroy]
    post 'students/:id' => 'students#create', as: :create_student

    resources :assistants, only: [:index, :create, :destroy]
    resources :lessons, only: [:index, :create, :destroy]
    resources :teachers, only: [:index, :create, :destroy, :update]
    resources :messages, only: [:create]
    resources :files, only: [:index, :create, :delete]
  end

  resources :lessons, only: [:show, :destroy] do
    # students' comments to the lesson
    resources :comments, controller: 'lesson_comments', only: [:index, :create]

    # for comment and score
    resources :students, only: [:show] do
      # assistants' comments to the student
      resources :comments, controller: 'student_comments', only: [:index, :create]
    end

    # for attendence
    resources :students, only: [:index, :create]

    resources :files, only: [:index, :create, :delete]
  end

  resources :students, only: [:index, :show, :create, :update, :destroy] do
    resources :courses, only: [:index, :update]
    post 'courses/:id' => 'courses#create', as: :create_course

    resources :files, only: [:index, :create, :destroy]
    resources :lessons, only: [:show, :update] do
      resources :files, only: [:show, :create, :destroy]
    end
    resources :messages, only: [:index]
  end

  resources :assistants do
    resources :courses, only: [:index, :create]
    resources :files, only: [:index, :create]
  end

  resources :teachers do
    resources :courses, only: [:index, :create]
  end

  resources :files, only: [:show, :create]

  namespace :android do
    get 'current_version.json' => 'android_apps#current_version'
    post 'current_version.json' => 'android_apps#create'
  end

  get 'test' => 'test#index'
  get 'manage' => 'manage#main_page'
  get 'manage/MainPage' => 'manage#main_page'
  get 'manage/CourseScore/:id' => 'manage#course_score'
  get 'manage/ClassScore/:id' => 'manage#class_score'
  get 'manage/CourseInfo/:id' => 'manage#course_info'
  get 'manage/ClassInfo/:id' => 'manage#class_info'
  get 'manage/ShowPhotos/:id' => 'manage#show_photos'
  get 'manage/ShowVideos/:id' => 'manage#show_videos'
  post 'manage/AddCourseToCurrentUser' => 'manage#AddCourseToCurrentUser'
end
