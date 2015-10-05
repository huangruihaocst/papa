Rails.application.routes.draw do

  devise_for :users

  resources :courses do
    resources :students
  end

  resources :lessons

end
