Rails.application.routes.draw do
  devise_for :users

  namespace :api do
    namespace :courses do
      root to: '/courses#api_index'
      get 'index'   => '/courses#api_index'
      get ':id'     => '/courses#api_show', constraints: { id: '[0-9]+' }
      post '/'      => '/courses#api_add'
    end

    namespace :lessons do
      root to: '/lessons#api_index'
      get 'index' => '/lessons#api_index'
      get ':id'   => '/lessons#api_show', constraints: { id: '[0-9]+' }
      post '/'    => '/lessons#api_add'
    end

  end
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'
  get 'test' => 'test#index'
  get 'manage' => 'manage#index'
  get 'manage/MainPage' => 'manage#main_page'
  get 'manage/CourseScore' => 'manage#course_score'
  get 'manage/ClassScore' => 'manage#class_score'
  get 'manage/CourseInfo' => 'manage#course_info'
  get 'manage/ShowPhotos' => 'manage#show_photos'
  get 'manage/ShowVideos' => 'manage#show_videos'
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

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
