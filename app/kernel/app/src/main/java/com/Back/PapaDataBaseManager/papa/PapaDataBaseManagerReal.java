package com.Back.PapaDataBaseManager.papa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.Activities.papa.receive_message.Message;
import com.Back.DataBaseAccess.papa.PapaDataBaseAccess;
import com.Back.DataBaseAccess.papa.PapaDataBaseInvalidFieldError;
import com.Back.DataBaseAccess.papa.PapaDataBaseJsonError;
import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.DataBaseAccess.papa.PapaDataBaseResourceNotFound;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Fragments.papa.experiment_result.Media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

//　走 HTTP 的实现.
public class PapaDataBaseManagerReal extends PapaDataBaseManager
{
    private Calendar getCalenderByString(String str)
            throws PapaHttpClientException
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.ENGLISH);

        Calendar ans = Calendar.getInstance();
        try
        {
            ans.setTime(sdf.parse(str));
        }
        catch(java.text.ParseException e)
        {
            e.printStackTrace();
            Log.e(tag, "Wrong date time format");
            throw new PapaDataBaseJsonError();
        }
        return ans;
    }

    final static String tag = "PapaDataBaseManagerReal";

    PapaDataBaseAccess dbAccess;

    public PapaDataBaseManagerReal()
    {
        dbAccess = new PapaDataBaseAccess();
    }

    @Override
    public SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();

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
        HashMap<String, Object> h = new HashMap<>();

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

            HashMap<String, Object> h = new HashMap<>();

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

            HashMap<String, Object> h = new HashMap<>();

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
            HashMap<String, Object> h = new HashMap<>();

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
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"),
                                new LessonInfo(
                                        obj.getString("name"),
                                        obj.getString("description"),
                                        getCalenderByString(obj.getString("start_time")),
                                        getCalenderByString(obj.getString("end_time")),
                                        obj.getString("location")
                                )
                        )
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
    public UsrInfoReply getUsrInfo(UsrInfoRequest request) throws PapaHttpClientException
    {
        try
        {
            HashMap<String, Object> h = new HashMap<>();
            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get, "/users/" + request.id + ".json", h
            );

            JSONObject obj = reply.getJSONObject("user");

            JSONObject avartarInfo = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/files/" + obj.getString("avator_id") + ".json", h
            );

            avartarInfo = avartarInfo.getJSONObject("file");
            String avaterURL = avartarInfo.getString("path");

            File file = new File(request.file, avartarInfo.getString("name"));
            dbAccess.getDataBaseAsFile(avaterURL, h, file);

            return new UsrInfoReply(
                    obj.getInt("id"),
                    new UsrInfo(
                            obj.getString("name"), obj.getString("email"), obj.getString("phone"),
                            file
                    )
            );
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }


    @Override
    public void putUsrInfo(PutUsrInfoRequest request) throws PapaHttpClientException
    {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("user[email]", request.usrInfo.mail);
        h.put("user[phone]", request.usrInfo.phone);
        h.put("user[name]", request.usrInfo.usrName);

        JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.put, "/users/" + request.id + ".json", h
        );
    }

    @Override
    public StudentsReply getStudents(StudentsRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();

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
                        new AbstractMap.SimpleEntry<>(obj.getInt("id"),
                                new StudentInfo(

                                        /*
                                        String studentId, String name, String studentNumber,
                                        String phone, String avatarId, String email,
                                        String description, String department, String className)
                                        */

                                        obj.getString("id"),
                                        obj.getString("name"),
                                        obj.getString("student_number"),
                                        obj.getString("phone"),
                                        obj.getString("avator_id"),
                                        obj.getString("email"),
                                        obj.getString("description"),
                                        obj.getString("department"),
                                        obj.getString("class_name")
                                )
                        )
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
            HashMap<String, Object> h = new HashMap<>();
            h.put("token", request.token);

            JSONObject reply_1;
            JSONObject reply_2;
            reply_2 = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.personId + ".json",
                    h
            );
            reply_2 = reply_2.getJSONObject("student_info");

            try {
                reply_1 = dbAccess.getDataBaseReplyAsJson(
                        PapaAbstractHttpClient.HttpMethod.get,
                        "/lessons/" + request.lessonId + "/students/" +
                                request.personId + "/comment.json",
                        null
                );
            }
            catch (PapaDataBaseResourceNotFound e)
            {
                return new GetCommentsReply(
                        reply_2.getString("student_number"),
                        reply_2.getString("department") + " " + reply_2.getString("class_name"),
                        null, null, null
                );
            }


            reply_1 = reply_1.getJSONObject("student_comment");


            return new GetCommentsReply(
                    reply_2.getString("student_number"),
                    reply_2.getString("department") + " " + reply_2.getString("class_name"),
                    reply_1.getString("score"),
                    reply_1.getString("content"),
                    reply_1.getString("creator_name")
            );
        }
        catch(org.json.JSONException e) {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void postTAComments(PostTACommentsRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("student_comment[score]", request.score);
        h.put("student_comment[content]", request.comments);

        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/lessons/" + request.lessonId + "/students/" +
                        request.personId + "/comments.json",
                h
        );
    }

    @Override
    public GetLessonInfoReply getLessonInfo(GetLessonInfoRequest request) throws
            PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();

            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/lessons/" + request.lessonId + ".json",
                    h
            );
            reply = reply.getJSONObject("lesson");

            return new GetLessonInfoReply(
                    reply.getString("name"),
                    getCalenderByString(reply.getString("start_time")),
                    getCalenderByString(reply.getString("end_time")),
                    reply.getString("location")
            );
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public GetMessagesIDReply getMessagesID(GetMessagesIDRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();

            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.personId + "/messages.json",
                    h
            );
            JSONArray array = reply.getJSONArray("messages");

            ArrayList<String> ans = new ArrayList<>();

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                ans.add(obj.getString("id"));

                Log.i(tag, "get msg where id = " +  obj.getString("id"));
            }

            return new GetMessagesIDReply(ans);
        }
        catch(org.json.JSONException e) {
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public GetMessageByIDReply getMessageByID(GetMessageByIDRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();

            h.put("token", request.token);
            JSONObject reply = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/messages/" + request.msgId + ".json",
                    h
            );

            reply = reply.getJSONObject("message");


            JSONObject reply_creator = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/users/" + reply.getString("creator_id") + ".json",
                    h
            );
            reply_creator = reply_creator.getJSONObject("user");


            JSONObject reply_course = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/courses/" + reply.getString("course_id") + ".json",
                    h
            );
            reply_course = reply_course.getJSONObject("course");


            Message msg = new Message(
                    reply.getString("id"),
                    reply.getString("title"),
                    reply.getString("message_type"),
                    reply.getString("content"),
                    getCalenderByString(reply.getString("deadline")),
                    reply_course.getString("name"),
                    reply_creator.getString("name")
            );

            return new GetMessageByIDReply(msg);
        }
        catch(org.json.JSONException e) {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void postStudentComments(PostStudentCommentsRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("lesson_comment[score]", request.score);
        h.put("lesson_comment[content]", request.comments);

        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/students/" + request.personId + "/lessons/" +
                        request.lessonId + "/comments.json",
                h
        );
    }


    @Override
    public GetStudentCommentsReply getStudentComments(GetStudentCommentsRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);


        JSONObject reply_creator = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.get,
                "/students/" + request.personId + "/lessons/" +
                        request.lessonId + "/comment.json",
                h
        );

        try
        {
            reply_creator = reply_creator.getJSONObject("lesson_comment");

            return new GetStudentCommentsReply(
                    reply_creator.getString("score"),
                    reply_creator.getString("content")
            );
        }
        catch(org.json.JSONException e)
        {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public PostFileOnLessonReply postFileOnLesson(PostFileOnLessonRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("file[type]", request.fileType);
        h.put("file[name]", request.fileName);
        h.put("file[file]", request.file);

        JSONObject object = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/students/" + request.personId + "/lessons/" +
                        request.lessonId + "/files.json",
                h
        );

        try
        {
            String id = object.getString("id");
            request.media.id = id;
            return new PostFileOnLessonReply(id);
        }
        catch(org.json.JSONException e)
        {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void postAttendance(PostAttendance request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
//        h.put("attendance[longitude]", String.valueOf(request.longitude));
//        h.put("attendance[latitude]", String.valueOf(request.latitude));
//        h.put("attendance[locationServiceAvailable]",
//                String.valueOf(request.locationServiceAvailable));

        // Edited by Alex Wang 2015-11-13. Change URL to predefined URL.
        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/lessons/" + request.lessonId + "/attendance.json",
                h
        );
    }

    @Override
    public void deleteAttendance(PostAttendance request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);

        // Edited by Alex Wang 2015-11-13. Change URL to predefined URL.
        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.delete,
                "/lessons/" + request.lessonId + "/attendance.json",
                h
        );
    }

//    @Override
//    public void deleteAttendance(PostAttendance request) throws PapaHttpClientException {
//        HashMap<String, Object> h = new HashMap<>();
//        h.put("token", request.token);
////        h.put("attendance[longitude]", String.valueOf(request.longitude));
////        h.put("attendance[latitude]", String.valueOf(request.latitude));
////        h.put("attendance[locationServiceAvailable]",
////                String.valueOf(request.locationServiceAvailable));
//
//        // Edited by Alex Wang 2015-11-13. Change URL to predefined URL.
//        dbAccess.getDataBaseReplyAsJson(
//                PapaAbstractHttpClient.HttpMethod.delete,
//                "/lessons/" + request.lessonId + "/attendance.json",
//                h
//        );
//    }

    @Override
    public GetChatMessageReply getChatMessages(GetChatMessageRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();
            h.put("token", request.token);
            JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/messages.json", h
            );

            ArrayList<ChatMessage> lst = new ArrayList<>();

            JSONArray array = obj.getJSONArray("user_messages");

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);


                lst.add(new ChatMessage(
                        object.getString("id"),
                        object.getString("creator_id"),
                        object.getString("creator_name"),
                        object.getString("title"),
                        object.getString("content"),
                        object.getString("status"),
                        getCalenderByString(object.getString("created_at"))
                ));
            }

            return new GetChatMessageReply(lst);
        }
        catch(org.json.JSONException e) {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void readChatMessages(ReadChatMessageRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);

        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/messages/" + request.messageId + "/read.json",
                h
        );
    }

    @Override
    public void postChatMessages(PostChatMessageRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("title", request.title);
        h.put("content", request.content);

        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/users/" + request.receiverId + "/messages.json",
                h
        );
    }

    @Override
    public GetTeachersInfoReply getTeachersInfo(GetTeachersInfoRequest request)
            throws PapaHttpClientException
    {
        try {
            List<TeacherInfo> lst = new ArrayList<>();

            HashMap<String, Object> h = new HashMap<>();

            h.put("token", request.token);

            JSONArray courses = new JSONArray();

            JSONArray studentCourses = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.personId + "/courses.json", h
            ).getJSONArray("courses");

            JSONArray assistantCourses = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/assistants/" + request.personId + "/courses.json", h
            ).getJSONArray("courses");

            for (int i = 0; i < studentCourses.length(); i++) {
                courses.put(studentCourses.get(i));
            }
            for (int i = 0; i < assistantCourses.length(); i++) {
                courses.put(assistantCourses.get(i));
            }

            HashSet<String> teachersIdSet = new HashSet<String>();

            for (int i = 0; i < courses.length(); i++) {
                JSONObject courseObject = courses.getJSONObject(i);
                String courseId = courseObject.getString("id");
                String courseName = courseObject.getString("name");

                JSONArray teacherArray = dbAccess.getDataBaseReplyAsJson(
                        PapaAbstractHttpClient.HttpMethod.get,
                        "/courses/" + courseId + "/teachers.json", null
                ).getJSONArray("teachers");

                Log.i(tag, "Teacher name list start");
                for(int j = 0; j < teacherArray.length(); j++) {
                    JSONObject teacherObject = teacherArray.getJSONObject(j);
                    if (teachersIdSet.add(teacherObject.getString("id"))) {
                        lst.add(new TeacherInfo(
                                        teacherObject.getString("name"),
                                        courseName,
                                        teacherObject.getString("phone"),
                                        teacherObject.getString("email"),
                                        teacherObject.getString("id")
                                )
                        );

                        Log.i(tag, "Teacher name = " + teacherObject.getString("name"));
                    }
                }
                Log.i(tag, "Teacher name list end");


            }


            return new GetTeachersInfoReply(lst);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();

        }
    }

    @Override
    public void PutUsrPassword(PutUsrPasswordRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("user[password]", request.password);

        dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.put, "/users/" + request.personId + ".json", h
        );
    }


    @Override
    public void postAvatar(PostAvatarRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);
        h.put("file[type]", request.fileType);
        h.put("file[name]", request.fileName);
        h.put("file[file]", request.file);

        JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.post,
                "/files.json",
                h
        );

        try
        {
            String id = obj.getString("id");
            h.clear();
            h.put("token", request.token);
            h.put("user[avator_id]", id);

            dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.put, "/users/" + request.personId + ".json", h
            );
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public void deleteFile(DeleteFileRequest request) throws PapaHttpClientException {
        HashMap<String, Object> h = new HashMap<>();
        h.put("token", request.token);

        /*
        JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.delete,
                "/students/" + request.personId + "/lessons/" + request.lessonId + "/files/" +
                        request.fileId + ".json",
                h
        );
        */

        JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                PapaAbstractHttpClient.HttpMethod.delete,
                "/files/" + request.fileId + ".json",
                h
        );
    }

    @Override
    public GetFilesReply getLessonFiles(GetFilesRequest request) throws PapaHttpClientException {
        try
        {
            HashMap<String, Object> h = new HashMap<>();
            h.put("token", request.token);

            JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/students/" + request.personId + "/lessons/" + request.lessonId + "/files.json",
                    h
            );

            JSONArray list = obj.getJSONArray("files");

            GetFilesReply reply = new GetFilesReply();

            for(int i = 0; i < list.length(); i++) {
                JSONObject fileObject = list.getJSONObject(i);
                String type = fileObject.getString("type");

                File file = new File(request.file, fileObject.getString("name"));
                dbAccess.getDataBaseAsFile(fileObject.getString("path"), h, file);

                Bitmap thumbnail;

                Media.Type t;
                if(type.equals("image"))
                {
                    t = Media.Type.image;
                    thumbnail = BitmapFactory.decodeFile(file.getAbsolutePath());
                    thumbnail = ThumbnailUtils.extractThumbnail(thumbnail,200,200);
                }
                else if(type.equals("video"))
                {
                    t = Media.Type.video;
                    thumbnail = ThumbnailUtils.createVideoThumbnail
                            (file.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
                }
                else continue;


                reply.mediaList.add(
                        new Media(
                                thumbnail,
                                file.getAbsolutePath(),
                                t,
                                fileObject.getString("id")
                        )
                );
            }

            return reply;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }

    @Override
    public GetLessonFilesReply getLessonFiles(GetLessonFilesRequest request) throws PapaHttpClientException {
        try {
            HashMap<String, Object> h = new HashMap<>();
            h.put("token", request.token);

            JSONObject obj = dbAccess.getDataBaseReplyAsJson(
                    PapaAbstractHttpClient.HttpMethod.get,
                    "/lessons/" + request.lessonId + "/files.json",
                    h
            );

            JSONArray list = obj.getJSONArray("files");

            GetLessonFilesReply reply = new GetLessonFilesReply();

            for (int i = 0; i < list.length(); i++) {
                JSONObject fileObject = list.getJSONObject(i);

                File file = new File(request.file, fileObject.getString("name"));
                dbAccess.getDataBaseAsFile(fileObject.getString("path"), h, file);

                reply.files.add(file);
            }

            return reply;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new PapaDataBaseJsonError();
        }
    }
}
