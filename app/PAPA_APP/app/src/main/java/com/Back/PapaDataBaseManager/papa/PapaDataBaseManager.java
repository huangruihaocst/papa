
/**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */
package com.Back.PapaDataBaseManager.papa;


import com.Activities.papa.message.Message;
import com.Back.NetworkAccess.papa.PapaHttpClientException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PapaDataBaseManager {


    // 登录
    static public class SignInRequest
    {
        public String id;
        public String pwd;

        public SignInRequest(String id, String pwd)
        {
            this.id = id;
            this.pwd = pwd;
        }
    }

    static public class SignInReply
    {
        public int personId;
        public String token;

        public SignInReply(int personId, String token)
        {
            this.personId = personId;
            this.token = token;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学期
    static public class SemesterReply
    {
        public List<Map.Entry<Integer, String>> semester;

        public SemesterReply()
        {
            semester = new ArrayList<>();
        }
    }

    public abstract SemesterReply getSemester() throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程 Course
    static public class CourseRequest
    {
        public int id;
        public int semesterId;
        public String token;

        public CourseRequest(int id, int semesterId, String token)
        {
            this.id = id;
            this.semesterId = semesterId;
            this.token = token;
        }
    }

    static public class CourseReply
    {
        public List<Map.Entry<Integer, String>> course;

        public CourseReply()
        {
            course = new ArrayList<>();
        }
    }

    public abstract CourseReply getStuCourse(CourseRequest request) throws PapaHttpClientException;
    public abstract CourseReply getTACourse(CourseRequest request) throws PapaHttpClientException;



    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程 Lesson
    static public class LessonRequest
    {
        public int courseId;

        public LessonRequest(int courseId)
        {
            this.courseId = courseId;
        }
    }

    static public class LessonReply
    {
        public List<Map.Entry<Integer, String>> lesson;

        public LessonReply()
        {
            lesson = new ArrayList<>();
        }
    }

    public abstract LessonReply getLesson(LessonRequest request) throws PapaHttpClientException;



    /////////////////////////////////////////////////////////////////////////////////////////////

    // 获取用户信息
    static public class UsrInfoRequest
    {
        public int id;
        public String token;

        public UsrInfoRequest(int id, String token)
        {
            this.id = id;
            this.token = token;
        }
    }

    static public class UsrInfoReply
    {
        public int id;
        public String usrName;
        public String mail;
        public String phone;

        public UsrInfoReply(int id, String usrName, String mail, String phone)
        {
            this.id = id;
            this.usrName = usrName;
            this.mail = mail;
            this.phone = phone;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract UsrInfoReply getUsrInfo(UsrInfoRequest request) throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////

    // 助教获取学生列表

    static public class StudentsRequest
    {
        public int courseId;
        public String token;

        public StudentsRequest(int courseId, String token)
        {
            this.courseId = courseId;
            this.token = token;
        }
    }

    static public class StudentsReply
    {
        public List<Map.Entry<Integer, String>> students;

        public StudentsReply()
        {
            students = new ArrayList<>();
        }
    }

    public abstract StudentsReply getStudents(StudentsRequest request)
            throws PapaHttpClientException;

    /*
        吐血推荐
        http://www.bilibili.com/video/av2098846/#page=2

        aLIEz + 霍元甲
        良心作
     */


    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取实验详细信息

    static public class GetLessonInfoRequest
    {
        public int lessonId;
        public String token;

        public GetLessonInfoRequest(int lessonId, String token)
        {
            this.lessonId = lessonId;
            this.token = token;
        }
    }

    static public class GetLessonInfoReply
    {
        public String name;
        public String startTime;
        public String endTime;
        public String location;
        public GetLessonInfoReply(String name, String startTime, String endTime, String location)
        {
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }
    }

    public abstract GetLessonInfoReply getLessonInfo(GetLessonInfoRequest request)
            throws PapaHttpClientException;

    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取助教对学生的评分

    static public class GetCommentsRequest
    {
        public int lessonId;
        public int personId;
        public String token;

        public GetCommentsRequest(int lessonId, int personId, String token)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
        }
    }

    static public class GetCommentsReply
    {
        public String stuId;
        public String className;
        public String score;
        public String comments;
        public GetCommentsReply(String stuId, String className, String score, String comments)
        {
            this.stuId = stuId;
            this.className = className;
            this.score = score;
            this.comments = comments;
        }
    }

    public abstract GetCommentsReply getComments(GetCommentsRequest request)
            throws PapaHttpClientException;

    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取助教对学生的评分

    static public class PostTACommentsRequest
    {
        public int lessonId;
        public int personId;
        public String score;
        public String comments;
        public String token;

        public PostTACommentsRequest(int lessonId, int personId, String token, String score, String comments)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
            this.score = score;
            this.comments = comments;
        }
    }

    public abstract void postTAComments(PostTACommentsRequest request)
            throws PapaHttpClientException;

    //////////////////////////////////////////////////////////
    // 获取推送列表


    static public class GetMessagesIDRequest
    {
        public int personId;
        public String token;

        public GetMessagesIDRequest(int personId, String token)
        {
            this.personId = personId;
            this.token = token;
        }
    }

    static public class GetMessagesIDReply
    {
        public List<String> msgIdLst;

        public GetMessagesIDReply(List<String> msgIdLst)
        {
            this.msgIdLst = msgIdLst;
        }
    }

    public abstract GetMessagesIDReply getMessagesID(GetMessagesIDRequest request)
            throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////
    // 根据推送 id 找详细信息

    static public class GetMessageByIDRequest
    {
        public String msgId;
        public String token;

        public GetMessageByIDRequest(String msgId, String token)
        {
            this.msgId = msgId;
            this.token = token;
        }
    }

    static public class GetMessageByIDReply
    {
        public Message msg;

        public GetMessageByIDReply(Message msg)
        {
            this.msg = msg;
        }
    }

    public abstract GetMessageByIDReply getMessageByID(GetMessageByIDRequest request)
            throws PapaHttpClientException;

    // どこまで叫べば位置を知れる

    //////////////////////////////////////////////////////////////////////////
    // 学生课程评价

    static public class PostStudentCommentsRequest
    {
        public int lessonId;
        public int personId;
        public String score;
        public String comments;
        public String token;

        public PostStudentCommentsRequest(int lessonId, int personId, String token, String score, String comments)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
            this.score = score;
            this.comments = comments;
        }
    }

    public abstract void postStudentComments(PostStudentCommentsRequest request)
            throws PapaHttpClientException;

    // 赤い　紅い　アカイ

    //////////////////////////////////////////////////////////////////////////
    // 学生课程评价

    static public class PostFileOnLessonAsStudentRequest
    {
        public int lessonId;
        public int personId;
        public String token;

        public String fileType;
        public String fileName;
        public File file;

        public PostFileOnLessonAsStudentRequest(
                int lessonId, int personId, String token, File file, String fileName, String fileType)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;

            this.fileType = fileType;
            this.fileName = fileName;
            this.file = file;
        }
    }

    public abstract void postFileOnLessonAsStudent(PostFileOnLessonAsStudentRequest request)
            throws PapaHttpClientException;



}