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
    gem install bundler
    bundle install
    rake db:migrate
    rake db:seed
    rails s
For production, use:

    #!/bin/sh
    gem install bundler
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
    status: 'successful'/'failed', // 代表获取成功与否
    reason: 'xxx',                 // 如果失败了， 这里显示失败原因
    \*CONTENT\*                    // 其余部分每个URL不同，对于每个URL请在下一章节看对应的数据格式
}

###登陆
POST /users/sign_in.json     utf8=✓&user[login]=xxx&user[password]=123&user[remember_me]=0
                                                ^                  ^
                                  此处可以是电话或者email    密码为明文，用https保证安全性 注意要 URL-Encode
                                  上面对号的utf-8编码值为0xE2 0x9C 0x93
返回json
{ 
    status: 'successful'/'faild',
    reason: 'invalid_password...', // 具体含义在后面介绍
    token: '123456777777777',
    id: 123
}
得到token字符串, 在以下url中会用到

###各个URL对应JSON文件格式, 以及各个URL功能简介

    标记方法：
    HTTP方法 URL                      功能                JSON格式(GET)/URL参数(POST/UPDATE/DELETE)     Token
        
    列表：
    GET    /courses.json             获得所有课程         "courses": [{"id": 1, "name": "xxx"}, ...]    No
    GET    /courses/1.json           获得ID为1的课程      "course": { "id": 1, "name": "xxx" }          No
    GET    /courses/1/students.json  获得id=1课的所有学生 "students": [{"id": 1, "name": "xx"}, ...]    No
    GET    /courses/1/assistants.json 类似上一个
  
    GET    /courses/1/lessons.json   获得id=1课所有实验课 "lessons": ["id": 1, "name": "xx"]            No

    GET    /students.json            获得（默认课程的）所有学生     "students": [{"id":1, "name": "xx"..]         No
    GET    /students/1.json          获得id=1学生的信息   "student": [{"id":1, "name": "xx"}, ..]       Student Token
    GET    /students/1/courses.json  获得id=1学生所有的课程 "courses": [{"id": 1, "name": "xxx"}, ..]   Student Token
    
    GET    /assistants.json          类似students
    GET    /assistants/1.json        类似students
    GET    /assistants/1/courses.json 类似students

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

###reason的可能值和含义
REASON_TOKEN_INVALID = 'token_invalid'      // token没有指定或者无效，应该检查url参数
REASON_TOKEN_TIMEOUT = 'token_timeout'      // token过时了，应该重新登陆
REASON_TOKEN_NOT_MATCH = 'token_not_match'  // id和token不匹配或者id不存在

##TODO
1.  Database Schema
1.  Unit Test
1.  Authentication Control.  
    