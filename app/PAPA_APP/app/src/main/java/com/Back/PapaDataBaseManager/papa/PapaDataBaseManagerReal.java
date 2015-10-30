package com.Back.PapaDataBaseManager.papa;

import android.util.Log;

import com.Back.DataBaseAccess.papa.PapaDataBaseAccess;
import com.Back.DataBaseAccess.papa.PapaDataBaseInvalidFieldError;
import com.Back.DataBaseAccess.papa.PapaDataBaseJsonError;
import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaHttpClientException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shyo on 15-10-22.
 */
public class PapaDataBaseManagerReal extends PapaDataBaseManager
{
    final static String tag = "PapaDataBaseManagerReal";

    PapaDataBaseAccess dbAccess;

    public PapaDataBaseManagerReal()
    {
        dbAccess = new PapaDataBaseAccess();
    }

    @Override
    public SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException {
        HashMap<String, String> h = new HashMap<>();

        h.put("utf8", "✓");
        h.put("user[login]", signInRequest.id);
        h.put("user[password]", signInRequest.pwd);
        h.put("user[remember_me]", "0");
        JSONObject replyObj;

        try
        {
            try {
                replyObj = dbAccess.getDataBaseReplyAsJson(
                        PapaAbstractHttpClient.HttpMethod.post,
                        "/users/sign_in.json", h
                );
            }
            catch (PapaDataBaseNotSuccessError e)
            {
                if(e.reason != null && e.reason.equals("invalid_field"))
                     throw new PapaDataBaseInvalidFieldError();
                throw e;
            }

            if(replyObj.getBoolean("is_admin")) throw new PapaDataBaseAdminError();
            if(replyObj.getBoolean("is_teacher")) throw new PapaDataBaseTeacherError();

            return new SignInReply(replyObj.getInt("id"), replyObj.getString("token"));
        }
        catch(org.json.JSONException e)
        {
            throw new PapaDataBaseJsonError();
        }
    }


    @Override
    public SemesterReply getSemester() throws PapaHttpClientException
    {
        try
        {
            SemesterReply ans = new SemesterReply();
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/semesters.json", null
            );

            JSONArray array = reply.getJSONArray("semesters");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                ans.semester.add(new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name")));

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            }

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    private Set<Integer> getSemesterCourse(CourseRequest request) throws
            PapaHttpClientException, org.json.JSONException
    {
        HashMap<String, String> h = new HashMap<>();

        h.put("token", request.token);

        JSONObject replySemesterCourse = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.get,
                "/semesters/" + request.semesterId + "/courses.json", h
        );

        JSONArray semesterCourseArray = replySemesterCourse.getJSONArray("courses");
        Log.i(tag, "ret = " + semesterCourseArray + " len = " + semesterCourseArray.length());

        Set<Integer> semesterCourse = new HashSet<>();
        for (int i = 0; i < semesterCourseArray.length(); i++)
        {
            JSONObject obj = semesterCourseArray.getJSONObject(i);
            semesterCourse.add(obj.getInt("id"));
        }
        return semesterCourse;
    }

    @Override
    public CourseReply getStuCourse(CourseRequest request) throws PapaHttpClientException {
        try
        {
            Set<Integer> semesterCourse = getSemesterCourse(request);

            HashMap<String, String> h = new HashMap<>();

            h.put("token", request.token);

            CourseReply ans = new CourseReply();
            JSONObject replyMine = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.id + "/courses.json", h
            );


            JSONArray array = replyMine.getJSONArray("courses");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (semesterCourse.contains(obj.getInt("id"))) {
                    ans.course.add(
                            new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                    );

                    Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
                }
            }

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }


    @Override
    public CourseReply getTACourse(CourseRequest request) throws PapaHttpClientException {
        try
        {
            Set<Integer> semesterCourse = getSemesterCourse(request);

            HashMap<String, String> h = new HashMap<>();

            h.put("token", request.token);

            CourseReply ans = new CourseReply();
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/assistants/" + request.id + "/courses.json", h
            );


            JSONArray array = reply.getJSONArray("courses");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                if (semesterCourse.contains(obj.getInt("id"))) {
                    ans.course.add(
                            new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                    );

                    Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
                }
            }

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public LessonReply getLesson(LessonRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();

            LessonReply ans = new LessonReply();
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/courses/" + request.courseId + "/lessons.json", h
            );


            JSONArray array = reply.getJSONArray("lessons");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                ans.lesson.add(
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                );

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            }

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }


    @Override
    public UsrInfoReply getUsrInfo(UsrInfoRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();
            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get, "/users/" + request.id + ".json", h
            );

            JSONObject obj = reply.getJSONObject("user");

            return new UsrInfoReply(
                    obj.getInt("id"), obj.getString("name"),
                    obj.getString("email"), obj.getString("phone")
            );
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public StudentsReply getStudents(StudentsRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();

            h.put("token", request.token);

            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/courses/" + request.courseId + "/students.json", h
            );


            StudentsReply ans = new StudentsReply();

            JSONArray array = reply.getJSONArray("students");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                ans.students.add(
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                );

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            }

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public GetCommentsReply getComments(GetCommentsRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();

            JSONObject reply_1 = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/lessons/" + request.lessonId + "/students/" +
                            request.personId + "/comment.json",
                    null
            );


            h.put("token", request.token);

            JSONObject reply_2 = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.personId + ".json",
                    h
            );

            reply_1 = reply_1.getJSONObject("student_comment");
            reply_2 = reply_2.getJSONObject("student");


            return new GetCommentsReply(
                    reply_2.getString("student_number"),
                    reply_2.getString("department") + " " + reply_2.getString("class_name"),
                    reply_1.getString("score"),
                    reply_1.getString("content")
            );
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void postComments(PostCommentsRequest request) throws PapaHttpClientException {
        HashMap<String, String> h = new HashMap<>();
        h.put("token", request.token);
        h.put("lesson_comment[score]", request.score.toString());
        h.put("lesson_comment[content]", request.comments);

        JSONObject reply_1 = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/lessons/" + request.lessonId + "/students/" +
                        request.personId + "/comments.json",
                h
        );
    }

    // どこまで叫べば位置を知れる　とどめもないまま息が切れる
    // 堂々さらした罪の群れと　後ろ向きにあらがう!!!

    @Override
    public GetLessonInfoReply getLessonInfo(GetLessonInfoRequest request) throws
            PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();

            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/lessons/" + request.lessonId + ".json",
                    h
            );
            reply = reply.getJSONObject("lesson");


            return new GetLessonInfoReply(
                    reply.getString("name"),
                    reply.getString("start_time"),
                    reply.getString("end_time"),
                    reply.getString("location")
            );
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

}
