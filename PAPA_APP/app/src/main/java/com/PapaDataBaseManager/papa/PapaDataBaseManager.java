
/**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */
package com.PapaDataBaseManager.papa;


import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.util.Log;


public class PapaDataBaseManager {
    private static final String host = "192.168.1.115";
    private static final String port = "80";
    private static final String tag = "PapaDataBaseManager";

    private String token;

    // 单件
    private PapaDataBaseManager()
    {
        token = null;
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

    // 执行 post 方法
    private static String excuteRequest(HttpRequestBase post)
    {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String content = EntityUtils.toString(response.getEntity(),"utf-8");
                return content;
            }else{
                Log.e(tag, "HTTP " + response.getStatusLine().getStatusCode());
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            Log.e(tag, e.getMessage().toString());

            // throw e;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.e(tag, e.getMessage().toString());

            // throw e;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(tag, e.getMessage().toString());

            // throw e;
        }

        return null;
    }


    // 登录线程
    private class LoginThread extends Thread
    {
        private String name;
        private String pwd;
        private boolean success;
        private String token;

        public boolean getSuccess()
        {
            return success;
        }

        public String getToken()
        {
            return token;
        }

        public LoginThread(String name, String pwd)
        {
            this.name = name;
            this.pwd = pwd;
            this.success = false;
        }

        public void run()
        {
            try {
                // 设置域名和 POST 参数
                // POS
                // utf8=✓&user[login]=xxx&user[password]=123&user[remember_me]=0

                String url = "http://" + host + ":" + port + "/users/sign_in.json";
                HttpPost post = new HttpPost(url);

                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("utf8", "✓"));
                params.add(new BasicNameValuePair("user[login]", name));
                params.add(new BasicNameValuePair("user[password]", pwd));
                params.add(new BasicNameValuePair("user[remember_me]", "0"));
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                Log.i(tag, "post = " + post.getURI().toString() + "  postData ");

                // 执行 POST
                String reply;
                Log.i(tag, "ret = " + (reply = excuteRequest(post)));

                if(reply == null)
                {
                    throw new Exception("nothing return");
                }

                // parse json
                JSONTokener jsonParser = new JSONTokener(reply);
                JSONObject replyObj = (JSONObject) jsonParser.nextValue();
                if(!replyObj.getString("status").equals("successful"))
                {
                    throw new Exception("not successful, ret \"" + replyObj.getString("status") + "\"");
                }

                // ok
                token = replyObj.getString("token");
                success = true;
                Log.i(tag, "login success in thread");
            }
            catch (Exception e)
            {
                Log.d(tag, "e.getCause()= " + e.getCause());
                Log.d(tag, "e.getMessage()= " + e.getMessage());
                // e.printStackTrace();
                Log.i(tag, "post fail");
            }
        }
    };

    // 使用 POST 方法登录
    public boolean signIn(String name, String password)
    {
        LoginThread thread = new LoginThread(name, password);

        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            Log.e(tag, "e.getMessage()= " + e.getMessage());
        }

        if(thread.getSuccess())
        {
            token = thread.getToken();
            Log.i(tag, "login success");
            return true;
        }

        return false;
    }

    // 获取课程线程
    private class GetCourseThread extends Thread
    {
        private boolean success;
        private String token;
        private String[] courses;

        public String[] getCourses()
        {
            return courses;
        }

        public boolean getSuccess()
        {
            return success;
        }

        public GetCourseThread(String token)
        {
            this.token = token;
            this.success = false;
        }

        public void run()
        {
            try {
                String url = "http://" + host + ":" + port + "/students/1/courses.json?";
                url += "token=" + token;

                HttpGet get = new HttpGet(url);


                Log.i(tag, "url = " + get.getURI().toString());

                // 执行请求
                String reply;
                Log.i(tag, "ret = " + (reply = excuteRequest(get)));

                if(reply == null)
                {
                    throw new Exception("nothing return");
                }

                // parse json
                JSONTokener jsonParser = new JSONTokener(reply);
                JSONObject replyObj = (JSONObject) jsonParser.nextValue();
                if(!replyObj.getString("status").equals("successful"))
                {
                    throw new Exception("not successful, ret \"" + replyObj.getString("status") + "\"");
                }

                JSONArray courseArray = replyObj.getJSONArray("courses");
                Log.i(tag, "ret = " + courseArray + " len = " + courseArray.length());

                courses = new String[courseArray.length()];
                for(int i = 0; i < courses.length; i++)
                {
                    courses[i] = courseArray.getJSONObject(i).getString("name");
                    Log.i(tag, "courses[i] = " + courses[i]);
                }

                success = true;
                Log.i(tag, "get course success in thread");
            }
            catch (Exception e)
            {
                Log.d(tag, "e.getCause()= " + e.getCause());
                Log.d(tag, "e.getMessage()= " + e.getMessage());
                // e.printStackTrace();
                Log.i(tag, "post fail");
            }
        }
    };

    public String[] getCourses()
    {
        GetCourseThread thread = new GetCourseThread(token);

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
            return thread.getCourses();
        }

        return null;
    }

}