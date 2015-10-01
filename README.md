#What's papdb?
Papdb is a back-end and an api provider for PapaCard App with simple web management pages.

##Development Environment
*   Ruby 2.2.2
*   Rails 4.2.4
*   sqlite3 - database back-end
*   devise 3.5.2 - authentication
*   jquery/bootstrap-sass - front-end frameworks

##How To Setup Development Environment
###For Ubuntu/Debian

#### 0. Download source code.
#### 1. Install RVM
    #!/bin/sh
    gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
    curl -sSL https://get.rvm.io | bash -s stable
or visit [rvm.io](http://rvm.io)
#### 2. Install Ruby
    #!/bin/sh
    bash -l
    rvm install 2.2.2
    rvm use 2.2.2
    ruby -v
    
#### 3. Configure project and database.
    #!/bin/sh
    bundle install
    rake db:migrate
    rake db:seed
    rails s
For production, use:

    #!/bin/sh
    bundle install
    rake db:migrate RAILS_ENV=production
    rake db:seed RAILS_ENV=production
    rake assets:precompile
    rails s -e production
and nginx configuration file:
    
    server {
      listen 80 default_server;
    
      root /xxx;
      index index.html;
    
      server_name localhost;
    
      location / {
        proxy_redirect off;
        proxy_pass http://localhost:3000;
      }

##Database schema
I'm busy...

##APIs Usage
Step1.  Token authentication
    POST    xxx

Step2.  Get data from these URLs

    # for courses
    GET /api/courses
    GET /api/courses/1
    GET /api/courses/name
    POST /api/courses
        
    
    # for lessons
    GET /api/lessons
    GET /api/lessons/1
    GET /api/lessons/lesson_full_name
    POST /api/lessons
        parameters: name, description, start_time, end_time, position, course_id
    
    # for users(including students and assistants)
    GET /api/users
    GET /api/users/1
    GET /api/users/name
        
or in rails way
    
    [Ruby code]
    namespace :api do
        resources :courses do 
            resources :lessons
        end
        resources :lessons do
            resources :students
        end
        resources :students do
            resources :lessons
        end
    end

##TODO
1.  Database Schema
1.  Unit Test
1.  Authentication Control.  
    