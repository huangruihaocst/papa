package com.Back.PapaDataBaseManager.papa;

import android.util.Log;

import com.Back.DataBaseAccess.papa.PapaDataBaseAccess;
import com.Back.DataBaseAccess.papa.PapaDataBaseJsonError;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaHttpClientException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.HashMap;

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
        HashMap<String, String> h = new HashMap<String, String>();

        h.put("utf8", "✓");
        h.put("user[login]", signInRequest.id);
        h.put("user[password]", signInRequest.pwd);
        h.put("user[remember_me]", "0");
        JSONObject replyObj;

        try
        {
            replyObj = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.post,
                    "/users/sign_in.json", h
            );


            if(replyObj.getBoolean("is_admin") == true) throw new PapaDataBaseAdminError();
            if(replyObj.getBoolean("is_teacher") == true) throw new PapaDataBaseTeacherError();

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

                ans.semester.put(obj.getInt("id"), obj.getString("name"));

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            };

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public CourseReply getStuCourse(CourseRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();

            h.put("token", request.token);

            CourseReply ans = new CourseReply();
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.id + "/courses.json", h
            );


            JSONArray array = reply.getJSONArray("courses");
            Log.i(tag, "ret = " + array + " len = " + array.length());

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                ans.course.add(
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                );

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            };

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
            HashMap<String, String> h = new HashMap<String, String>();

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

                ans.course.add(
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"), obj.getString("name"))
                );

                Log.i(tag, obj.getInt("id") + " " + obj.getString("name"));
            };

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
            };

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }


    @Override
    public GetUsrInfoReply getUsrInfo(GetUsrInfoRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, String> h = new HashMap<>();
            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get, "/users/" + request.id + ".json", h
            );

            JSONObject obj = reply.getJSONObject("user");
            GetUsrInfoReply ans = new GetUsrInfoReply(
                    obj.getInt("id"), obj.getString("name"),
                    obj.getString("email"), obj.getString("phone")
            );

            return ans;
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
            };

            return ans;
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }

    }
}
