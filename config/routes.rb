Rails.application.routes.draw do

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  resources :courses do
    resources :students
    resources :assistants
    resources :lessons
  end
  resources :students do
    resources :courses
  end
  resources :assistants do
    resources :courses
  end

  resources :lessons
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'
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
  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end
end
