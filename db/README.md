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

###各个URL对应JSON文件格式, 以及各个URL功能简介
标注:
    1. xxx parameter代表http参数，在后边会说明
    2. xxx 代表xxx类对象的json，在后面说明
    3. \[xxx, ...\] 代表xxx类对象数组的json，在后面说明
    4. 注意: #!表示已经弃用的api, ?代表新增加的api
    5. 每个API下面的缩进行代表错误返回信息的定义
    6. 如果url错误, 均返回失败, reason: invalid_operation
    
    标记方法：
    HTTP方法 URL                      功能                JSON格式(GET)/URL参数(POST/UPDATE/DELETE)     Token
    
    # namespace user
    # 用户相关
    GET    /users/1.json             得到用户的信息     "user": user
    POST    /users/sign_in.json       登陆               utf8=✓&user[login]=xxx&user[password]=123&user[remember_me]=0
        返回:  token: '123456777777777', id: 123
        *上面对号的utf-8编码值为0xE2 0x9C 0x93                                                          Everyone
    DELETE  /users/sign_out.json      注销                                                             User       
        暂时不检查错误
    POST    /users.json               注册普通用户, 非(老师或管理员)                                    Everyone
        not_implemented
    PUT     /users/1.json             修改用户信息                                                      User
        not_implemented
    GET    /users/current.json       获取当前用户id          "id": "123"                                 Current User
        permission_denied: 未登录

    # namespace semesters
    GET    /semesters.json               获得所有学年     "semesters": [semester, ...]                  Student
        always successful
    POST   /semesters.json               添加一个学年     semester parameters                           Admin
        permission_denied: 不是管理员
        internal_error: 其他内部错误, 如果遇到请联系我
    PUT    /semesters/1.json             修改学年信息     semester parameters                           Admin
        permission_denied: 不是管理员
        resource_not_found: semester_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    DELETE /semesters/1.json             删除某个学年     id                                            Admin
        permission_denied: 不是管理员
        resource_not_found: semester_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /semesters/1/courses.json     获得某个学年的所有课程 "courses": [course, ...]                Student
        resource_not_found: semester_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /semesters/default.json       获得默认学期     "semester": semester                          Student 
        invalid_fields: 总学年数为0
    
    # namespace courses
    # 课程相关
    PUT    /courses/1.json           修改课程             course parameters                             Teacher
        permission_denied: 当前用户不是该课程的老师
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /courses/1.json           获得ID为1的课程      "course": { "id": 1, "name": "xxx" }          Student
        resource_not_found: invalid course_id
    DELETE /courses/1.json           删除课程             id                                            Teacher
        permission_denied: 当前用户不是该课程的老师
        internal_error: 其他内部错误, 如果遇到请联系我

    # 与课程有关系的资源
    GET    /courses/1/teachers.json  获取该门课所有老师   "teachers": { id, ... }                        Student
        invalid_operation: course_id未指定
        resource_not_found: course_id不存在
    POST   /courses/1/teachers/1.json 把老师添加到课程                                                  Admin
        permission_denied: 不是管理员
        resource_not_found: course_id或teacher_id不存在
    DELETE /courses/1/teachers/1.json 从课程中删除老师       id                                          Admin
        permission_denied: 不是管理员
        resource_not_found: course_id或teacher_id不存在
    GET    /courses/1/students.json  获得id=1课的所有学生 "students": [student, ...]                     Teacher
        resource_not_found: course_id不存在
        permission_denied: 不是该课程的老师
    GET    /courses/1/assistants.json 类似上一个          "assistants": [assistant, ...]                 Teacher(Student?)
        resource_not_found: course_id不存在
        permission_denied: 不是该课程的老师
    POST   /courses/1/students/1.json 添加学生到指定课程                                                Teacher
        resource_not_found: course_id不存在
        permission_denied: 不是该课程的老师
        internal_error: 其他内部错误, 如果遇到请联系我
    ?POST   /courses/1/students.json                 json=[{username: "", email: "", phone: "", student_no: ""}...] Teacher
        失败:
            resource_not_found: course_id不存在
            token_invalid: 未登录
            permission_denied: 当前用户不是该门课程的老师
            format_error: json格式错误
            invalid_fields: student_number未指定
        成功:
            部分学生添加失败 invalid_fields: ["123", "111"...] 数字为student_number
            
    POST   /courses/1/assistants/1.json 添加助教到指定课程                                              Teacher
        not_implemented
    GET    /courses/1/lessons.json   获得id=1课所有实验课 "lessons": ["id": 1, "name": "xx"]             Student
        resource_not_found: course_id不存在
    POST   /courses/1/lessons.json   向课程中添加实验课                                                  Teacher
        permission_denied: 不是该课程的老师
        invalid_fields: lesson参数无效
    GET    /courses/1/comments.json  得到某门课程所有学生的所有评论                                      Teacher
        not_implemented(罗干要求)
    
    # namespace lessons
    GET    /lessons/1.json           获得某门实验课的信息  "lesson": lesson                              Student
        resource_not_found: lesson_id不存在
    PUT     /lessons/1.json           修改实验课信息
        not_implemented
    DELETE /lessons/1.json          删掉实验课
        permission_denied: 不是该课程的老师
        resource_not_found: lesson_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /lessons/1/comments.json  获得某门课程的评价    "lesson_comments": [lesson_comment, ...]     Teacher
        暂时不检查错误
    POST   /lessons/1/start_sign_up.json      发起签到                                                  Teacher
        not_in_lesson_time: 当前不是实验课时间

    GET   /lessons/1/students/1/comment.json  助教对学生的评价 "student_comment": student_comment
        暂时不检查错误
    POST   /lessons/1/students/1/comments.json 助教对学生的评价 student comment parameters              Assistant
        暂时不检查错误
    GET    /lessons/1/students.json  某门实验课的到课学生列表 students id                                Teacher
        not_implemented
    #!POST   /lessons/1/students/1.json 学生签到                                                          Student
        resource_not_found: lesson_id或者student_id找不到
        permission_denied: 当前学生id不匹配
    ?POST   /lessons/1/attendance.json 学生签到                                                          Student
        resource_not_found: lesson_id或者student_id找不到
        permission_denied: 当前学生id不匹配
        
    GET    /lessons/1/files.json     获得该门实验课的简介文件
        resource_not_found: lesson_id不存在
        invalid_fields: file参数或type参数未指定或者格式不正确
        file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
    POST   /lessons/1/files.json     向课程中添加文件
        resource_not_found: lesson_id不存在
        **注意, 如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
   
    # namespace students
    # 学生相关
    GET    /students/1.json          获得id=1学生的信息   "student": [{"id":1, "name": "xx"}, ..]        Student
        permission_denied: 未登录
        resource_not_found: student_id不存在
    GET    /students/1/files.json    获得学生所有文件列表   "files": [{"id":1, "type": "jpg", "path": "xx"}...]  Student 
        permission_denied: 当前用户不是student_id
        **注意, 如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
    POST   /students/1/files.json    学生添加文件                                                        Student
        resource_not_found: student_id不存在
        permission_denied: student_id和当前用户不匹配, 或者lesson_id不属于student_id
        invalid_fields: file参数或type参数未指定或者格式不正确
        file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
    DELETE /students/1/files/1.json  删除文件                                                            Creator
        resource_not_found: file_id不存在
        permission_denied: 当前用户不是文件创建者
        **注意这里忽略student_id
    GET     /students/1/lessons/1/comment.json 某位学生对课程的评价
        resource_not_found: student_id或者lesson_id不存在或者该学生不在该课程中
    POST    /students/1/lessons/1/comments.json
        resource_not_found: student_id或者lesson_id不存在或者该学生不在该课程中
        
    # 课程/实验课相关
    GET    /students/1/courses.json  获得所有课程           "courses": [course...]                       Student
        permission_denied: student_id和当前登陆用户不匹配
        resource_not_found: student_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    POST   /students/1/courses/1.json       给学生添加课程                                               Student
        permission_denied: student_id和当前登陆用户不匹配
        resource_not_found: student_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    DELETE /students/1/courses/1.json       把学生从课程中移除                                            Teacher
        permission_denied: 当前用户不是该课程的老师
        resource_not_found: student_id不存在或者course_id不存在或者student_id和不在course_id的学生列表中
        internal_error: 其他内部错误, 如果遇到请联系我

    GET    /students/1/lessons/1/files.json 获得某门实验课某个学生的所有文件 "files": [file, ...]         Student
        permission_denied: student_id和当前登陆用户不匹配
        **注意, 如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
        
    POST   /students/1/lessons/1/files.json 在某门实验课上添加视频图片                                    Student
        permission_denied: 一下三条至少有一条不满足:
            1. 用户已经登陆或当前token有效
            2. student_id包含在lesson_id对应的课程的学生名单中 ***(或者当前用户是课程的老师)
            3. 当前用户是student_id或当前用户在lesson_id的助教名单中
        invalid_fields: file参数或type参数未指定或者格式不正确
        file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
    
    DELETE /students/1/lessons/1/files/1.json 在某门实验课上删除视频照片
        resource_not_found: file_id不存在
        permission_denied: 当前用户不是文件创建者
        **注意这里忽略student_id和lesson_id
    
    GET    /assistants.json           获得所有助教信息      "assistant": [assistant, ...]                Student
        not_implemented(该API是否有用?)
    GET    /assistants/1.json         获得某个助教信息      "assistant": assistant                       Assistant
        resource_not_found: 助教不存在
        permission_denied: 当前用户不是assistant_id
    GET    /assistants/1/courses.json 获得某个助教的所有课程 "courses": [course, ...]                    Assistant
        permission_denied: assistant_id和当前用户不匹配
        resource_not_found: assistant_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    POST  /assistants/1/courses/1.json 给某个助教添加某门课                                             Assistant
        permission_denied: assistant_id和当前用户不匹配
        resource_not_found: assistant_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /assistants/1/files.json   助教上传的所有文件    "files": [file, ...]                         Assistant
        permission_denied: 当前用户和assistant_id不匹配
        resource_not_found: assistant_id不存在
        
    # 老师相关
    GET    /teachers/1.json           获得某个老师的信息    "teacher": teacher                           Student
        resource_not_found: teacher_id不存在
    POST   /teachers.json             添加老师              teacher parameters                           Admin
        not_implemented
    DELETE /teachers/1.json           删除老师              id                                           Admin
        resource_not_found: teacher_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    UPDATE /teachers/1.json           修改老师信息          teacher parameters                           The Teacher
        not_implemented
    GET    /teachers/1/courses.json   获得老师的所有课程    "courses": [course, ...]                     Student
        permission_denied: teacher_id和当前登陆用户不匹配
        resource_not_found: teacher_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    POST   /teachers/1/courses.json   给老师添加课程        course parameters                            Teacher
        permission_denied: teacher_id和当前登陆用户不匹配
        resource_not_found: teacher_id不存在
        internal_error: 其他内部错误, 如果遇到请联系我
    GET    /teachers/1/lessons.json     获得一个老师的所有实验课
        resource_not_found: teacher_id不存在

    # 消息推送
    GET    /students/1/messages.json  查询学生的所有消息     "messages": [message ...]                   Student
        resource_not_found: student_id不存在
        permission_denied: student_id和当前用户不匹配
    POST   /courses/1/messages.json   发送消息到某门课的所有学生 "message": message                      Assistant
        resource_not_found: course_id不存在
        permission_denied: 当前用户不是course_id的老师且不是course_id的助教
        invalid_field: message创建参数取值范围或类型错误
    
    # 文件相关
    GET    /files/1.json             得到该文件的信息        "file": file
        resource_not_found: file_id不存在
    POST   /files.json               上传文件                file parameters 
        permission_denied: 未登录
        file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
    DELETE /files/1.json             删除自己上传的文件                                                  Creator           
        resource_not_found: file_id不存在
        permission_denied: 当前用户不是文件创建者
        
    # 学生老师发消息
    POST    /users/1/messages.json   当前用户给该用户发送消息
        resource_not_found: user_id 不存在
        too_often: 发送消息太频繁
    GET     /messages.json           获取当前用户的所有消息
        permission_denied: 未登录
    POST    /messages/1/read.json
        resource_not_found
    GET     /messages/new_messages_count.json
        permission_denied: 未登录
    DELETE  /messages/1.json        
        not_implemented
    
    # App更新
    GET    /android/current_version.json 得到当前最新版本号  {"version": "xx", "apk_path": "xx"}         Student
        always successful
    POST   /android/current_version.json 上传apk            version=xx apk                              Admin
        permission_denied: 当前用户不是管理员
        
    # 快捷查询
    GET    /courses/1.json?full=true
        { status: successful,
          course: { xx, 和从前一样, ..., students: [{ id: 1, name: '123', email: , phone: ,
           comment: { score: , content: , creator_name  } } ]} 
    GET    /lessons/1.json?full=true
       
Deprecated APIs:

    #!POST   /lessons/1/comments.json  添加学生对课程的评价(当前登陆用户)  lesson comment parameters        Student
    #!DELETE /courses/1/students/1.json 删除学生
    #!GET    /courses.json             获得所有课程(包含所有学年的) "courses": [course, ...]               Student
    #!POST   /courses.json             添加课程             course parameters                             Teacher
    #!POST   /courses/1/teachers.json  添加老师             teacher parameters                            Teacher
    #!GET    /students/1/lessons/1.json       获得学生课程的信息，包括成绩     "course": course_score       Student
    #!PUT    /students/1/lessons/1.json       修改学生课程信息                                             Assistant  
    #!PUT    /students/1/courses.json  修改学生课程列表                                                    Teacher
    #!POST   /students.json            添加一个学生         student parameters                            Teacher
    #!PUT    /students/1.json          修改学生             student parameters                            Student
    #!DELETE /students/1.json          删除学生             id                                            Teacher
    #!GET    /students.json            获得（默认课程的）所有学生     "students": [{"id":1, "name": "xx"..] 
    #!POST   /courses/1/assistants.json 添加助教            id                                            Teacher
    #!DELETE /courses/1/assistants/1.json 删除助教
    #!GET    /lessons/1/comments.json  获得某门课程的评价    "comments": [comment, ...]                   Teacher
    #!GET    /lessons/1/students/1/comments.json 查看助教对学生的评价 "student_comments": [student_comment,...] Student
    #!DELETE /courses/1/lessons/1.json 从课程中删除实验课
    #!PUT    /courses/1/teachers.json  修改老师             teacher parameters                            Teacher
    #!POST   /courses/1/students.json  添加学生              
    #!GET    /students/1/lessons/1/comments.json 查看学生对课程的所有评价 "lesson_comments": [student_comment,...] Teacher

API Status:

    Total History:      84
    Deprecated:         18
    Total Valid:        66
    Not Implemented:    10
    Not Secured:        6
    Fully Finished:     50
        
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
            #!class=string,
            ?class_name=string,
            department=string,
            #!avator_path=string
            ?avator_id=int
             
    assistant id=int,
            name=string,
            phone=string,
            email=string,
            #!avator_path=string
            ?avator_id=int
    
    teacher id=int
            name=string
            email=string
            phone=string
            avator_id=int
            description=string
            
    lesson_comment id=int        // 学生对课程的评价
            content=string
            score=string
            ?creator_name=string
            ?created_at=datetime
            
    student_comment id=int      // 助教对学生的评价
            content=string
            score=string(0-10)
            ?creator_name=string
            
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
    
    user_message id=int
            title=string
            content=string
            sender_id=int
            receiver_id=int
            status=string [read|unread]
            
###reason的可能值和含义
REASON_PERMISSION_DENIED = 'permission_denied'
REASON_TOKEN_INVALID = 'token_invalid'
REASON_TOKEN_TIMEOUT = 'token_timeout'
REASON_TOKEN_NOT_MATCH = 'token_not_match'
REASON_NOT_IMPLEMENTED = 'not_implemented'
REASON_INVALID_OPERATION = 'invalid_operation'
REASON_RESOURCE_NOT_FOUND = 'resource_not_found'
REASON_INTERNAL_ERROR = 'internal_error'

# json parameters when a user post an invalid json.
REASON_INVALID_FIELD = 'invalid_field'
...其他值可以在相应的API处找到

##TODO
1. Simplify Code
2. Optimize Code/Database
    
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
            
