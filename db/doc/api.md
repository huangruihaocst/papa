Restful API
---
1. 表示法介绍
---

下表为所有API的使用方法，分别有ID， HTTP方法，URL，参数，返回值，错误，权限和注释

参数：标准的http参数

返回值：每个API返回均为JSON，包含一个status属性， 如果成功为success，失败为fail，如果发生错误
status属性会是错误信息，在“错误”中介绍。

权限：分为student|assistant|teacher|admin，权限依次增大，
“权限”一列表示的是所需的最低权限，如果权限不够会返回错误。

**有些接口没有包含参数的注释，这是因为这是一个创建对象或者修改对象的方法，参数列表在后面“对象的属性”一章介绍。
**有些接口没有包含返回值，那么默认就是返回status是success或者fail
**有些接口的返回值包含一个对象，对象的属性在后面“对象的属性”一章介绍
2. API列表
---

|ID | HTTP方法| 地址           |  注释 |  权限 |
|-- |:--------| ---------------| ----  | -----:|
| 用户相关 |||||
|1  |GET    |/users/1.json  | 得到用户的信息 |     "user": user |
|2  |POST   |/users/sign_in.json|       登陆 | |
|   | 参数  |user[login]=xxx&user[password]=123&user[remember_me]=0  |||
|   | 返回  |token: '123456777777777', id: 123 |||
|3  |DELETE |/users/sign_out.json|      注销 | User       
|   | 错误  | 暂时不检查错误 |||
|4  |POST   | /users.json | 注册普通用户, 非(老师或管理员) | Everyone |
|   |not_implemented|||
|5  |PUT    |/users/1.json|修改用户信息|User
|   |not_implemented|||
|8  |GET    |/users/current.json | 获取当前用户id | Current User
|   |返回值 | "id": "123"|||
|   |错误   |permission_denied: 未登录 |||
| 学期相关 |||||
|10 |GET    |/semesters.json|获得所有学年| Student
|   |返回值 | "semesters": [semester, ...] |||
|   |错误   |always successful |||
|11 |POST   |/semesters.json    |添加一个学年 |Admin                        
|   |错误   |permission_denied: 不是管理员 |||
|   |       |internal_error: 其他内部错误, 如果遇到请联系开发者 |||
|12 |PUT    |/semesters/1.json  |修改学年信息 |Admin
|   |错误   |permission_denied: 不是管理员 |||
|   |       |resource_not_found: semester_id不存在 |||
|   |       |internal_error: 其他内部错误, 如果遇到请联系我 |||
|13 |DELETE |/semesters/1.json  |删除某个学年|Admin
|   |错误   |permission_denied: 不是管理员 |||
|   |       |resource_not_found: semester_id不存在 |||
|   |       |internal_error: 其他内部错误, 如果遇到请联系我 |||
|14 |GET    |/semesters/1/courses.json  |获得某个学年的所有课程    |Student
|   |参数   |"courses": [course, ...]|||
|   |错误   |resource_not_found: semester_id不存在 |||
|   |       |internal_error: 其他内部错误, 如果遇到请联系我 |||
|15 |GET    |/semesters/default.json    |获得默认学期 |Student 
|   |参数   |"semester": semester |||
|   |错误   |invalid_fields: 总学年数为0 |||
| 课程相关 |||||
|20 |PUT    |/courses/1.json    |修改课程   |Teacher
|   |错误   |permission_denied: 当前用户不是该课程的老师
|   |       |internal_error: 其他内部错误, 如果遇到请联系我
|21 |GET    |/courses/1.json    |获得ID为1的课程  |Student
|   |返回值 |Course对象
|   |错误值 |resource_not_found: invalid course_id
|22 |DELETE |/courses/1.json    |删除课程   |Teacher
|   |错误   |permission_denied: 当前用户不是该课程的老师 |||
|   |       |internal_error: 其他内部错误, 如果遇到请联系我 |||
| 课程相关 |||||
|23 |GET    |/courses/1/teachers.json       |获取该门课所有老师  |Student
|   |返回值 |Teacher数组
|   |错误   |invalid_operation: course_id未指定 |||
|   |       |resource_not_found: course_id不存在 |||
|24 |POST   |/courses/1/teachers/1.json     |把老师添加到课程   |Admin
|   |错误   |permission_denied: 不是管理员
|   |       |resource_not_found: course_id或teacher_id不存在
|25 |DELETE |/courses/1/teachers/1.json     |从课程中删除老师   |Admin
|   |错误   |permission_denied: 不是管理员
|   |       |resource_not_found: course_id或teacher_id不存在
|26 |GET    |/courses/1/students.json       |获得id=1课的所有学生   |Teacher
|   |返回值 |"students": [student, ...]
|   |错误   |resource_not_found: course_id不存在
|   |       |permission_denied: 不是该课程的老师
|27 |GET    |/courses/1/assistants.json     |类似上一个         |Student
|   |返回值 |"assistants": [assistant, ...]                 
|   |错误   |resource_not_found: course_id不存在
|   |       |permission_denied: 不是该课程的老师
|28 |POST   |/courses/1/students/1.json     |添加学生到指定课程 |Teacher
|   |错误   |resource_not_found: course_id不存在
|   |       |permission_denied: 不是该课程的老师
|   |       |internal_error: 其他内部错误, 如果遇到请联系我
|29 |POST   |/courses/1/students.json
|   |参数   |User对象， 必填：username，email，phone，student_no|Teacher
|   |错误   |resource_not_found: course_id不存在
|   |       |token_invalid: 未登录
|   |       |permission_denied: 当前用户不是该门课程的老师
|   |       |format_error: json格式错误
|   |       |invalid_fields: student_number未指定
|   |\*\*注释|部分学生添加失败 invalid_fields: ["123", "111"...] 数字为student_number
|   |       |批量添加学生, 默认密码是学号
| 助教相关 |||||
|30 |POST   |/courses/1/assistants/1.json   |添加助教到指定课程  |Teacher
|   |错误   |resource_not_found: course_id不存在
|   |       |permission_denied: 不是该课程的老师
|   |       |internal_error: 其他内部错误, 如果遇到请联系我
|31 |POST   |/courses/1/assistants.json    |批量添加助教        |Teacher
|   |参数   |User对象， 必填：username，email，phone
|   |错误   |resource_not_found: course_id不存在
|   |       |token_invalid: 未登录
|   |       |permission_denied: 当前用户不是该门课程的老师
|   |       |format_error: json格式错误
|   |       |invalid_fields: student_number未指定
|   |       |批量添加助教, 默认密码是email
|32 |GET    |/courses/1/lessons.json        |获得id=1课所有实验课|Student
|   |返回值 |Lesson数组
|   |错误   |resource_not_found: course_id不存在
|33 |POST   |/courses/1/lessons.json        |向课程中添加实验课  |Teacher
|   |错误   |permission_denied: 不是该课程的老师
|   |       |invalid_fields: lesson参数无效
| 课程相关 |||||
|40 |GET    |/lessons/1.json                |获得某门实验课的信息|Student
|   |错误   |resource_not_found: lesson_id不存在
|41 |DELETE |/lessons/1.json                |删掉实验课         |Teacher
|   |错误   |permission_denied: 不是该课程的老师
|   |       |resource_not_found: lesson_id不存在
|   |       |internal_error: 其他内部错误, 如果遇到请联系我
|42 |GET    |/lessons/1/comments.json  获得某门课程的评价        |Teacher
|43 |POST   |/lessons/1/start_sign_up.json  |发起签到           |Teacher
|   |错误   |not_in_lesson_time: 当前不是实验课时间
| 对学生的评价相关 |||||
|44 |GET   |/lessons/1/students/1/comment.json  |助教对学生的评价   |Student
|45 |POST  |/lessons/1/students/1/comments.json |添加助教对学生的评价|Assistant
|46 |GET   |/lessons/1/students.json            |某门实验课的到课学生列表|Teacher
|47 |POST  |/lessons/1/attendance.json          |学生签到           |Student
|   |错误  |resource_not_found: lesson_id或者student_id找不到
|   |      |permission_denied: 当前学生id不匹配
|48 |DELETE|/lessons/1/attendance.json          |登出
| 文件相关  |||||
|50 |GET   |/lessons/1/files.json               |获得该门实验课的简介文件 |Student
|   |错误  |resource_not_found: lesson_id不存在
|   |      |invalid_fields: file参数或type参数未指定或者格式不正确
|   |      |file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
|51 |POST  |/lessons/1/files.json               |向课程中添加文件        |Teacher
|   |错误  |resource_not_found: lesson_id不存在
|   |\*\*注意|如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
| 学生相关 |||||
|60 |GET   |/students/1.json                    |获得id=1学生的信息      |Student
|   |错误  |permission_denied: 未登录
|   |      |resource_not_found: student_id不存在
|61 |GET   |/students/1/files.json              |获得学生所有文件列表    |Student 
|   |返回值|FileResource数组
|   |错误  |permission_denied: 当前用户不是student_id
|   |\*\*注意|如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
|62 |POST  |/students/1/files.json              |学生添加文件            |Student
|   |错误  |resource_not_found: student_id不存在
|   |      |permission_denied: student_id和当前用户不匹配, 或者lesson_id不属于student_id
|   |      |invalid_fields: file参数或type参数未指定或者格式不正确
|   |      |file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
|63 |DELETE|/students/1/files/1.json            |删除文件                |Creator
|   |错误  |resource_not_found: file_id不存在
|   |      |permission_denied: 当前用户不是文件创建者
|   |\*\*注意|这里忽略student_id
|64 |GET   |/students/1/lessons/1/comment.json 某位学生对课程的评价
|   |错误  |resource_not_found: student_id或者lesson_id不存在或者该学生不在该课程中
|65 |POST  |/students/1/lessons/1/comments.json
|   |错误  |resource_not_found: student_id或者lesson_id不存在或者该学生不在该课程中
| 课程/实验课相关 |||||
|70 |GET   |/students/1/courses.json            |获得所有课程            |Student
|   |错误  |permission_denied: student_id和当前登陆用户不匹配
|   |      |resource_not_found: student_id不存在
|   |      |internal_error: 其他内部错误, 如果遇到请联系我
|71 |POST  |/students/1/courses/1.json          |给学生添加课程          |Student
|   |错误  |permission_denied: student_id和当前登陆用户不匹配
|   |      |resource_not_found: student_id不存在
|   |      |internal_error: 其他内部错误, 如果遇到请联系我
|72 |DELETE|/students/1/courses/1.json          |把学生从课程中移除       |Teacher
|   |错误  |permission_denied: 当前用户不是该课程的老师
|   |      |resource_not_found: student_id不存在或者course_id不存在或者student_id和不在course_id的学生列表中
|   |      |internal_error: 其他内部错误, 如果遇到请联系我
|73 |GET   |/students/1/lessons/1/files.json    |获得某门实验课某个学生的所有文件 |Student
|   |错误  |permission_denied: student_id和当前登陆用户不匹配
|   |\*\*注意|如果lesson_id不正确或者超出范围, 不会返回错误, 只会查不到数据
|74 |POST  |/students/1/lessons/1/files.json    |在某门实验课上添加视频图片       |Student
|   |错误  |permission_denied: 一下三条至少有一条不满足:
|   |      ||1. 用户已经登陆或当前token有效
|   |      ||2. student_id包含在lesson_id对应的课程的学生名单中 \*\*\*(或者当前用户是课程的老师)
|   |      ||3. 当前用户是student_id或当前用户在lesson_id的助教名单中
|   |      |invalid_fields: file参数或type参数未指定或者格式不正确
|   |      |file_too_big: 文件大小超过指定大小(100M, 该数值请查看config/initializers/constants.rb)
|75 |DELETE|/students/1/lessons/1/files/1.json |在某门实验课上删除视频照片       |Creator
|   |错误  |resource_not_found: file_id不存在
|   |      |permission_denied: 当前用户不是文件创建者
|   |\*\*注意|这里忽略student_id和lesson_id
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
    
3. 对象属性
---

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