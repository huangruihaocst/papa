
/**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */
package com.Back.PapaDataBaseManager.papa;


import android.util.Log;

import com.Back.DataBaseAccess.papa.DataBaseAccess;
import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.UnknownMethodException;

import org.json.JSONObject;

import java.util.HashMap;

public class PapaDataBaseManager {

    private static final String tag = "PapaDataBaseManager";

    private int personID;
    private String token;

    DataBaseAccess dbAccess;

    // 单件
    private PapaDataBaseManager()
    {
        token = null;
        dbAccess = new DataBaseAccess();
    }

    private static PapaDataBaseManager instance = null;

    public static synchronized PapaDataBaseManager getInstance()
    {
        if(instance == null)
        {
            instance = new PapaDataBaseManager();
        }

        return instance;
    }


    // 使用 POST 方法登录
    public boolean signIn(String name, String password) throws org.json.JSONException, UnknownMethodException
    {
        HashMap<String, String> h = new HashMap<String, String>();

        h.put("utf8", "✓");
        h.put("user[login]", name);
        h.put("user[password]", password);
        h.put("user[remember_me]", "0");
        JSONObject replyObj;
        try {
            replyObj = dbAccess.getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod.post, "/users/sign_in.json", h);
        }
        catch (PapaDataBaseNotSuccessError e)
        {
            Log.e(tag, e.getMessage().toString());
            return false;
        }
        this.token = replyObj.getString("token");
        this.personID = replyObj.getInt("id");

        return true;
    }

    /*
    // 获取课程线程
    private class GetCourseThread extends Thread
    {
        private boolean success;
        private String token;
        private List<String> studentCourses;
        private List<String> assistCourses;

        public boolean getSuccess()
        {
            return success;
        }

        public GetCourseThread(List<String> studentCourses, List<String> assistCourses, String token)
        {
            this.token = token;
            this.studentCourses = studentCourses;
            this.assistCourses = assistCourses;
            this.success = false;
        }

        private void iGetCourses(String url, List<String> lst) throws Exception
        {
            HttpGet get = new HttpGet(url);

            Log.i(tag, "url = " + get.getURI().toString());

            // 执行请求
            String reply;
            Log.i(tag, "ret = " + (reply = executeRequest(get)));

            if (reply == null) {
                throw new Exception("nothing return");
            }

            // parse json
            JSONTokener jsonParser = new JSONTokener(reply);
            JSONObject replyObj = (JSONObject) jsonParser.nextValue();
            if (!replyObj.getString("status").equals("successful")) {
                throw new Exception("not successful, ret \"" + replyObj.getString("status") + "\"");
            }

            JSONArray courseArray = replyObj.getJSONArray("courses");
            Log.i(tag, "ret = " + courseArray + " len = " + courseArray.length());

            for (int i = 0; i < courseArray.length(); i++)
            {
                lst.add(courseArray.getJSONObject(i).getString("name"));
                Log.i(tag, "JSONObject ins " + lst.get(lst.size() - 1));
            }
        }

        public void run()
        {
            try {
                iGetCourses("http://" + host + ":" + port + "/students/" + personID + "/courses.json?" + "token=" + token, studentCourses);
                iGetCourses("http://" + host + ":" + port + "/assistants/" + personID + "/courses.json?" + "token=" + token, assistCourses);

                success = true;
                Log.i(tag, "get course success in thread");
            }
            catch (Exception e)
            {
                Log.d(tag, "e.getCause()= " + e.getCause());
                Log.d(tag, "e.getMessage()= " + e.getMessage());
                Log.i(tag, "post fail");
            }
        }
    };

    public void getCourses(List<String> studentCourses, List<String> assistCourses)
    {
        studentCourses.clear();
        assistCourses.clear();

        GetCourseThread thread = new GetCourseThread(studentCourses, assistCourses, token);

        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            Log.e(tag, "e.getMessage() = " + e.getMessage());
        }
        if(thread.getSuccess())
        {
            Log.i(tag, "get course success");
        }
    }
    */

}