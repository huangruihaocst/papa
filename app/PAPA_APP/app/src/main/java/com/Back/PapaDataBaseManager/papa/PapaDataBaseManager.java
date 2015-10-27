
/**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */
package com.Back.PapaDataBaseManager.papa;


import com.Back.NetworkAccess.papa.PapaHttpClientException;

import java.util.ArrayList;
import java.util.HashMap;
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
        public HashMap semester;

        public SemesterReply()
        {
            semester = new HashMap();
        }
    }

    public abstract SemesterReply getSemester() throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程 Course
    static public class CourseRequest
    {
        public int id;
        public String token;

        public CourseRequest(int id, String token)
        {
            this.id = id;
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
    static public class GetUsrInfoRequest
    {
        public int id;
        public String token;

        public GetUsrInfoRequest(int id, String token)
        {
            this.id = id;
            this.token = token;
        }
    }

    static public class GetUsrInfoReply
    {
        public int id;
        public String usrName;
        public String mail;
        public String phone;

        public GetUsrInfoReply(int id, String usrName, String mail, String phone)
        {
            this.id = id;
            this.usrName = usrName;
            this.mail = mail;
            this.phone = phone;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract GetUsrInfoReply getUsrInfo(GetUsrInfoRequest request) throws PapaHttpClientException;


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

    // 使用 POST 方法登录 返回是否成功
    public abstract StudentsReply getStudents(StudentsRequest request) throws PapaHttpClientException;

}