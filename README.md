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
标注:
    1. xxx parameter代表http参数，在后边会说明
    2. xxx 代表xxx类对象的json，在后面说明
    3. \[xxx, ...\] 代表xxx类对象数组的json，在后面说明
    4. 注意: #!表示已经弃用的api, ?代表新增加的api
    
    标记方法：
    HTTP方法 URL                      功能                JSON格式(GET)/URL参数(POST/UPDATE/DELETE)     Token
    
    列表
    # namespace semesters
    GET    /semesters.json               获得所有学年     "semesters": [semester, ...]                  Student
    POST   /semesters.json               添加一个学年     semester parameters                           Admin
    PUT    /semesters/1.json             修改学年信息     semester parameters                           Admin
    DELETE /semesters/1.json             删除某个学年     id                                            Admin
    GET    /semesters/1/courses.json     获得某个学年的所有课程 "courses": [course, ...]                 Student
    GET    /semesters/default.json       获得默认学期     "semester": semester                          Student   
    
    # namespace courses
    # 课程相关
    #!GET    /courses.json             获得所有课程(包含所有学年的) "courses": [course, ...]               Student
    #!POST   /courses.json             添加课程             course parameters                             Teacher
    PUT    /courses/1.json           修改课程             course parameters                             Teacher
    DELETE /courses/1.json           删除课程             id                                            Teacher
    GET    /courses/1.json           获得ID为1的课程      "course": { "id": 1, "name": "xxx" }          Student
    
    # 与课程有关系的资源
    GET    /courses/1/teachers.json  获取该门课所有老师   "teachers": { id, ... }                        Student
    #!POST   /courses/1/teachers.json  添加老师             teacher parameters                            Teacher
    ?POST   /courses/1/teachers/1.json 把老师添加到课程
    #!PUT    /courses/1/teachers.json  修改老师             teacher parameters                            Teacher
    DELETE /courses/1/teachers/1.json 删除老师            id                                            Teacher
    
    GET    /courses/1/students.json  获得id=1课的所有学生 "students": [student, ...]                    Assistant
    #!POST   /courses/1/students.json  添加学生              
    DELETE /courses/1/students/1.json 删除学生
    ?POST   /courses/1/students/1.json 添加学生到指定课程
    GET    /courses/1/assistants.json 类似上一个          "assistants": [assistant, ...]                Student
    #!POST   /courses/1/assistants.json 添加助教            id                                            Teacher
    #!DELETE /courses/1/assistants/1.json 删除助教
    ?POST   /courses/1/assistants/1.json 添加助教到指定课程                                              Teacher
    
    GET    /courses/1/lessons.json   获得id=1课所有实验课 "lessons": ["id": 1, "name": "xx"]            Student
    POST   /courses/1/lessons.json   向课程中添加实验课
    #!DELETE /courses/1/lessons/1.json 从课程中删除实验课
    ?DELETE /lessons/1.json          删掉实验课
    
    # namespace lessons
    GET    /lessons/1.json           获得某门实验课的信息  "lesson": lesson                             Student
    #!GET    /lessons/1/comments.json  获得某门课程的评价    "comments": [comment, ...]                   Teacher
    ?GET    /lessons/1/comments.json  获得某门课程的评价    "lesson_comments": [lesson_comment, ...]                   Teacher
    POST   /lessons/1/comments.json  添加学生对课程的评价(当前登陆用户)  lesson comment parameters                    Student
    GET    /lessons/1/students/1/comments.json 查看助教对学生的评价 "student_comments": [student_comment,...] Student
    POST   /lessons/1/students/1/comments.json 助教对学生的评价 student comment parameters              Assistant
    GET    /lessons/1/students.json  某门实验课的到课学生列表 students id                               Teacher
    POST   /lessons/1/students/1.json 学生签到                                                          Student
    GET    /lessons/1/files.json     获得该门实验课的简介文件
    POST   /lessons/1/files.json     向课程中添加文件
    
    # namespace students
    # 学生相关
    GET    /students.json            获得（默认课程的）所有学生     "students": [{"id":1, "name": "xx"..] 
    GET    /students/1.json          获得id=1学生的信息   "student": [{"id":1, "name": "xx"}, ..]       Student
    #!POST   /students.json            添加一个学生         student parameters                            Teacher
    #!PUT    /students/1.json          修改学生             student parameters                            Student
    #!DELETE /students/1.json          删除学生             id                                            Teacher
    
    # 文件相关
    GET    /students/1/files.json    获得学生所有文件列表   "files": [{"id":1, "type": "jpg", "path": "xx"}...]  Student 
    POST   /students/1/files.json    学生添加文件                                                        Student
    DELETE /students/1/files/1.json  删除文件                                                            Creator
    
    # 课程实验课相关
    GET    /students/1/courses.json  获得所有课程           "courses": [course...]                       Student
    PUT    /students/1/courses.json  修改学生课程列表                                                    Teacher
    GET    /students/1/lessons/1.json       获得学生课程的信息，包括成绩     "course": course_score       Student
    PUT    /students/1/lessons/1.json       修改学生课程信息                                             Assistant  
    GET    /students/1/lessons/1/files.json 获得某门实验课某个学生的所有文件 "files": [file, ...]         Student
    POST   /students/1/lessons/1/files.json 在某门实验课上添加视频图片                                      Student
    DELETE /students/1/lessons/1/files/1.json 在某门实验课上删除视频照片
    
    GET    /assistants.json           获得所有助教信息      "assistant": [assistant, ...]                Student
    GET    /assistants/1.json         获得某个助教信息      "assistant": assistant                       Assistant
    GET    /assistants/1/courses.json 获得某个助教的所有课程 "courses": [course, ...]                    Assistant
    GET    /assistants/files.json     助教上传的所有文件    "files": [file, ...]                         Assistant
    
    GET    /teachers/1.json           获得某个老师的信息    "teacher": teacher                           Student
    POST   /teachers.json             添加老师              teacher parameters                           Admin
    DELETE /teachers/1.json           删除老师              id                                           Admin
    UPDATE /teachers/1.json           修改老师信息          teacher parameters                           The Teacher
    GET    /teachers/1/courses.json   获得老师的所有课程    "courses": [course, ...]                     Student
    POST   /teachers/1/courses.json   给老师添加课程        course parameters                            Teacher
    
    # 消息推送
    GET    /students/1/messages.json  查询学生的所有消息     "messages": [message ...]               Student
    POST   /courses/1/messages.json   发送消息到某门课的所有学生 "message": message                      Assistant
    
    # 文件相关
    GET    /files/1.json             得到该文件的信息        "file": file
    POST   /files.json               上传文件                file parameters 
    DELETE /files/1.json             删除自己上传的文件                                                  Creator           
    
    # 用户相关
    GET    /users/current.json       获取当前用户id          "id": "123"                                 Current User
    
    # App更新
    GET    /android/current_version.json 得到当前最新版本号  {"version": "xx", "apk_path": "xx"}         Student
    POST   /android/current_version.json 上传apk            version=xx apk                              Admin
    
Http Parameters/JSON对象格式
    
    注意：以semester举例,在URL中格式均为
    semester[name]=2009
    
    semester id=int
            name=string
    
    course  id=int,
            name=string,
            semester=int,
            description=string
            
    lesson  id=int, 
            name=string,   
            description=string,
            start_time=datetime
            end_time=datetime
            location=string
            attached_file_id=string
    
    student id=int,
            name=string,
            student_number=int,
            phone=string,
            email=string,
            class=string,
            department=string,
            avator_path=string
             
    assistant id=int,
            name=string,
            phone,
            email=string,
            avator_path=string
            
    lesson_comment id=int        // 学生对课程的评价
            content=string
            score=string
            
    student_comment id=int      // 助教对学生的评价
            content=string
            score=string(0-10)
            
    message id=int
            type=string(homework|notification),
            title=string,
            deadline=datetime,
            content=string,
            user_id=int
            
    file    id=int
            type=string
            name=string
            path=string(not required)
            
###reason的可能值和含义
REASON_TOKEN_INVALID = 'token_invalid'      // token没有指定或者无效，应该检查url参数
REASON_TOKEN_TIMEOUT = 'token_timeout'      // token过时了，应该重新登陆
REASON_TOKEN_NOT_MATCH = 'token_not_match'  // id和token不匹配或者id不存在

##TODO
1.  Unit Test
1.  Authentication Control.  
    
    
##Design

1. Database Schema

    semester id=int 
            name=string,
            has_many courses
    
    course  id=int,
            name=string,
            description=string
            ,
            belongs_to semester,
            has_many lessons,
            has_many participations,
            has_many students, through: participation, as: :user
            has_many teachers, through: teaching_course
            has_many messages
     
    participation id=int,
            role=string
            ,
            belongs_to course,
            belongs_to user
            
    lesson  id=int, 
            name=string,   
            description=string,
            start_time=datetime,
            end_time=datetime,
            location=string,
            ,
            belongs_to course
            has_many attached_files, as: :files
            has_many lesson_comments,
            has_many student_comments
    lesson_files id=int
            belongs_to lesson
            belongs_to file_resource
    student_files id=int
            belongs_to student
            belongs_to file_resource
            belongs_to lesson
    assistant_files id=int
            belongs_to assistant
            belongs_to file_resource
            belongs_to lesson
            
    user    id=int,
            name=string,
            student_number=int,
            phone=string,
            email=string,
            class=string,
            department=string
            ,
            has_many lesson_comments
            belongs_to avator, as: :file
            has_many teaching_courses
            has_many assisting_courses
            has_many learning_courses
            has_many lesson_statuses
            has_many assistant_to_student_comments, as: student_comments, foreign_key: :creator_id
            has_many from_assistant_comments, as: student_comments, foreign_key: :student_id
            has_many posted_messages, as: :message, foreign_key: :creator_id
     
    teaching_course id=int
            belongs_to user
            belongs_to course
    assisting_course id=int
            belongs_to user
            belongs_to course
    learning_course id=int
            belongs_to user
            belongs_to course
    lesson_status id=int
            score=string
            belongs_to user
            belongs_to lesson
            timestamps

    lesson_comment id=int,        // 学生对实验课的评价
            content=string,
            score=int
            timestamps
            ,
            belongs_to creator, as: :user,
            belongs_to lesson

    student_comment id=int,       // 助教对学生的评价
            content=string,
            score=int(0-10)
            timestamps
            ,
            belongs_to lesson
            belongs_to creator, as: :user,
            belongs_to student, as: :user,
            
    message id=int,
            type=string(homework|notification),
            title=string,
            deadline=datetime,
            content=string
            timestamps
            ,
            belongs_to creator, as: :user
            belongs_to course
           
    file    id=int,
            type=string,
            name=string,
            path=string,
            timestamps
            ,
            belongs_to creator, as: :user
            
    android_app id=int,
            version=string,
            timestamps
            ,
            belongs_to file
            