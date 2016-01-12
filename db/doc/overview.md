总览
---

##1. 总体结构
---
使用了Ruby On Rails框架，该框架采用MVC架构


#TODO rails架构图


网页端和App端共用一套JSON API
支持使用cookie和token的认证方式，网页端用cookie认证，App端用token认证。

##2. 目录结构

    app/                    应用程序主要代码
    app/assets/             动态资源文件（动态js，css）
    app/controllers/        MVC的控制器部分
    app/models/             MVC的模型部分
    app/views/              MVC的视图部分
    bin/                    rails的可执行文件
    config/                 配置文件
    config/routes.rb        路由配置（URL和controller方法的对应关系）
    config/initializers/    初始化配置和常量
    db/                     数据库
    db/migrate              数据库结构文件
    doc/                    文档
    log/                    日志
    public/                 静态资源文件的目录
    tmp/                    运行时临时文件
    libs/ vendor/           第三方库
    

##3. 安装，配置
---
需求

*   Ruby 2.2以上
*   Rails 4.2.4以上
*   sqlite3
*   devise > 3.5.2
*   bootstrap-sass 3

###Ubuntu/Debian
#### 0. 下载源码
#### 1. 安装RVM来管理ruby
    #!/bin/sh
    gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
    curl -sSL https://get.rvm.io | bash -s stable
    或者参照[rvm.io](http://rvm.io)
#### 2. 安装Ruby
    #!/bin/sh
    bash -l
    rvm install 2.2.2
    rvm use 2.2.2
    ruby -v
    
#### 3. 安装依赖库和配置数据库
    #!/bin/sh
    gem install bundler
    bundle install
    rake db:migrate
    rake db:seed
    rails s

接下来的操作为生产环境的操作， 开发环境需要吧RAILS_ENV这句去掉

    开发环境：
    #!/bin/sh
    gem install bundler
    bundle install
    rake db:migrate RAILS_ENV=production
    rake db:seed RAILS_ENV=production
    rake assets:precompile
    rails s -e production
    
nginx的配置文件（静态文件用nginx来调度）:

    server {
      listen 80 default_server;
    
      root /xxx;
      index index.html;
    
      server_name localhost;
    
      location / {
        proxy_redirect off;
        proxy_pass http://localhost:3000;
      }
