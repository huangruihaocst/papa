package com.PapaDataBaseManager.papa; /**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.util.Log;


public class PapaDataBaseManager {

    private static final String TAG = "PhoneSMSTest";

    // 使用 POST 方法登录
    public boolean signIn(String name, String password)
    {
        StringBuilder buf = new StringBuilder();

        // POS
        // utf8=✓&user[login]=xxx&user[password]=123&user[remember_me]=0
        try
        {

            buf.append("utf8=✓&user[login]=" + URLEncoder.encode(name, "UTF-8") + "&");
            buf.append("user[password]=" + URLEncoder.encode(password, "UTF-8") + "&");
            buf.append("user[remember_me]=0");
        }
        catch(Exception e)
        {
            return false;            // Always must return something
        }
        // Log.(TAG, "PhoneSMSTest....");
        /*

        byte[]data = buf.toString().getBytes("UTF-8");
        URL url = new URL("http://192.168.0.103:8080/Server/PrintServlet");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true); //如果要输出，则必须加上此句
        OutputStream out = conn.getOutputStream();
        out.write(data);
        if(conn.getResponseCode()==200){
            Toast.makeText(MainActivity.this, "GET提交成功", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(MainActivity.this, "GET提交失败", Toast.LENGTH_SHORT).show();
        */

        return true;
    }


}