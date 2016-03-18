类说明
===

1. Controllers（位于app/controllers）
    
    a. SemestersController
        响应和学期有关的页面，负责处理Semester model
    b. CoursesController
        课程（每学期的课程）
    c. LessonsController
        实验课（对应每一节课）
    e. LessonCommentsController
        学生对课程的评价
    f. StudentCommentsController
        助教/老师对学生的评价
    g. MessagesController
        老师向同学们推送消息
    h. UserMessagesController
        同学和老师之间互相发送消息，包括请假，批假
    i. TeachersController
        老师
    j. AssistantsController
        助教
    k. StudentsController
        学生
    l. User
        登录用户
    m. AttendanceController
        管理学生上课的信息

2. Models（位于app/models）
    
    每个Model对应数据库的一张表。

3. Views（位于app/views）

    由于该应用程序是主要功能是提供JSON API，所以View主要功能是根据Controller的处理结果，构造出一个JSON文件。

4. Routes（位于config/routes.rb）
    
    管理URL和Controller方法之间的对应关系。
    
5. 杂项
    
    所有常量均位于config/initializers/constants.rb