
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

    // 获取学生课程
    static public class GetCourseRequest
    {
        public int id;
        public String token;

        public GetCourseRequest(int id, String token)
        {
            this.id = id;
            this.token = token;
        }
    }

    static public class GetCourseReply
    {
        public List<Map.Entry<Integer, String>> course;

        public GetCourseReply()
        {
            course = new ArrayList<>();
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract GetCourseReply getStuCourse(GetCourseRequest request) throws PapaHttpClientException;
    public abstract GetCourseReply getTACourse(GetCourseRequest request) throws PapaHttpClientException;



    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程
    static public class GetLessonRequest
    {
        public int courseId;

        public GetLessonRequest(int courseId)
        {
            this.courseId = courseId;
        }
    }

    static public class GetLessonReply
    {
        public List<Map.Entry<Integer, String>> lesson;

        public GetLessonReply()
        {
            lesson = new ArrayList<>();
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract GetLessonReply getLesson(GetLessonRequest request) throws PapaHttpClientException;


}