package com.Back.NetworkAccess.papa;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaApacheHttpClient extends PapaAbstractHttpClient {
    final static String tag = "PapaApacheHttpClient";

    // 执行 post 方法
    private static String executeRequest(HttpRequestBase request) throws org.apache.http.client.ClientProtocolException, java.io.IOException, PapaHttpClientException
    {
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);

        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            return content;
        }else{
            Log.e(tag, "HTTP " + response.getStatusLine().getStatusCode() + ", NOT 200 OK");
            throw new PapaHttpClientException();
        }

        /*
        catch (UnsupportedEncodingException e) {
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
        */
    }

    @Override
    public String getHttpReplyByGet(String url, HashMap<String, String> parameter) {
        // 准备 url
        if(!parameter.isEmpty())
        {
            url += '?';

            boolean first = true;

            Iterator it = parameter.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();

                url += key + '=' + parameter.get(key);
                if(first)
                    first = false;
                else
                    url += '&';
            }
        }

        HttpGet get = new HttpGet(url);

        Log.i(tag, "url = " + get.getURI().toString());

        // 执行请求
        String reply = null;
        try {
            Log.i(tag, "ret = " + (reply = executeRequest(get)));
        }
        catch(Exception e)
        {

        }

        return reply;
    }

    @Override
    public String getHttpReplyByPost(String url, HashMap<String, String> parameter) {
        HttpPost post = new HttpPost(url);

        ArrayList params = new ArrayList();
        if(!parameter.isEmpty())
        {
            Iterator it = parameter.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                params.add(new BasicNameValuePair(key, parameter.get(key)));
            }
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            Log.e(tag, e.getMessage().toString());
        }

        Log.i(tag, "post = " + post.getURI().toString() + "  postData ");

        // 执行请求
        String reply = null;
        try {
            Log.i(tag, "ret = " + (reply = executeRequest(post)));
        }
        catch(Exception e)
        {
            Log.e(tag, e.getMessage().toString());
        }

        return reply;
    }
}
