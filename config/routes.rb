Rails.application.routes.draw do

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  resources :semesters, only: [:index, :create, :update, :delete] do
    resources :courses, only: [:index]
    get 'default' => 'semesters#default'
  end

  resources :courses, only: [:index, :show, :create, :update, :delete] do
    resources :students, only: [:index, :create, :delete]
    resources :assistants, only: [:index, :create, :delete]
    resources :lessons, only: [:index, :create, :delete]
    resources :teachers, only: [:index, :create, :delete, :update]
    resources :messages, only: [:create]
  end

  resources :lessons, only: [:show, :create, :index] do
    # students' comments to the lesson
    resources :comments, controller: 'lesson_comments', only: [:index, :create]

    # for comment and score
    resources :students, only: [:show] do
      # assistants' comments to the student
      resources :comments, controller: 'student_comments', only: [:index, :create]
    end

    # for attendence
    resources :students, only: [:index, :create]
  end

  resources :students, only: [:index, :show, :create, :update, :delete] do
    resources :courses, only: [:index, :update]
    resources :files, only: [:index, :create, :delete]
    resources :lessons, only: [:show, :update] do
      resources :files, only: [:show, :create, :delete]
    end
    resources :messages, only: [:index]
  end

  resources :assistants do
    resources :courses
    resources :files
  end

  resources :files, only: [:show, :create]

  namespace :android do
    get 'current_version.json' => 'android_apps#current_version'
    post 'current_version.json' => 'android_apps#current_version'
  end


  get 'test' => 'test#index'
  get 'manage' => 'manage#main_page'
  get 'manage/MainPage' => 'manage#main_page'
  get 'manage/CourseScore/:id' => 'manage#course_score'
  get 'manage/ClassScore/:id' => 'manage#class_score'
  get 'manage/CourseInfo/:id' => 'manage#course_info'
  get 'manage/ShowPhotos/:id' => 'manage#show_photos'
  get 'manage/ShowVideos/:id' => 'manage#show_videos'
  post 'manage/AddCourseToCurrentUser' => 'manage#AddCourseToCurrentUser'
end
