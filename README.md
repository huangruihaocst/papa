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

##Android端API使用方法

###基础
Android客户端通过访问指定的URL获得一个JSON文件来访问数据库。

该JSON文件格式为：
{ 
    status: 'successful'/'failed' // 代表获取成功与否
    reason: 'xxx'                 // 如果失败了， 这里显示失败原因
    \*CONTENT\*                   // 其余部分每个URL不同，对于每个URL请在下一章节看对应的数据格式
}

###各个URL对应JSON文件格式, 以及各个URL功能简介

    标记方法：
    HTTP方法 URL              功能             JSON格式(GET)/URL参数(POST/UPDATE/DELETE)
        
    
    列表：
    GET    /courses.json             获得所有课程         "courses": [{"id": 1, "name": "xxx"}, ...]
    GET    /courses/1.json           获得ID为1的课程      "course": { "id": 1, "name": "xxx" }
    GET    /courses/1/students.json  获得id=1课的所有学生 "students": [{"id": 1, "name": "xx"}, ...]
  
    GET    /courses/1/lessons.json   获得id=1课所有实验课 "lessons": ["id": 1, "name": "xx"]

    GET    /students.json            获得所有学生         "students": [{"id":1, "name": "xx"..]
    GET    /students/1.json          获得id=1学生的信息   "student": [{"id":1, "name": "xx"}, ..]
    GET    /students/1/courses.json  获得id=1学生所有的课程 "courses": [{"id": 1, "name": "xxx"}, ..]
   
举例：得到一个课程id=1的所有学生列表
   
```Java
[Java code]
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

class Main {
    void main(String args[]) {
        HttpGet httpRequest = new HttpGet("http://localhost:3000/courses/1/students.json");
        HttpResponse httpResponse = httpClient.execute(httpRequest);  
        // if successful
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            strResult = EntityUtils.toString(httpResponse.getEntity());
            // here, strResult is
            //  {
            //      "status": "successful",
            //      "students": [
            //          { "id": 1, "name": "Alex Wang" },
            //          { "id": 2, "name": "Fuck Shit" }
            //      ]
            //  }
        }
    }
}

```

##TODO
1.  Database Schema
1.  Unit Test
1.  Authentication Control.  
    