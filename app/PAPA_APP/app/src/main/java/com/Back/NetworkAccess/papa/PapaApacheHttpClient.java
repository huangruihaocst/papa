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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaApacheHttpClient extends PapaAbstractHttpClient
{
    final static String tag = "PapaApacheHttpClient";

    // 执行 get / post 方法
    private static String executeRequest(HttpRequestBase request)
        throws PapaHttpClientException
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 3 * 1000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        try {
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                return content;
            } else {
                Log.e(tag, "HTTP " + response.getStatusLine().getStatusCode() + ", NOT 200 OK");
                throw new PapaHttpClientNot200Exception(response.getStatusLine().getStatusCode());
            }
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
            Log.e(tag, e.getMessage().toString());
            throw new PapaHttpClientIOErrorException();
        }
    }

    @Override
    protected String getHttpReplyByGet(String url, HashMap<String, String> parameter)
        throws PapaHttpClientException
    {
        // 准备 url
        if(parameter != null && !parameter.isEmpty())
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

        Log.i(tag, "ret = " + (reply = executeRequest(get)));


        Log.i(tag, reply.toString());

        return reply;
    }

    @Override
    protected String getHttpReplyByPost(String url, HashMap<String, String> parameter)
        throws PapaHttpClientException
    {
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

        Log.i(tag, "post = " + post.getURI().toString() + ", postData ");

        // 执行请求
        String reply = null;
        Log.i(tag, "ret = " + (reply = executeRequest(post)));
        Log.i(tag, reply.toString());
        return reply;
    }

    // 单件
    private static PapaApacheHttpClient instance = null;

    private PapaApacheHttpClient()
    {
    }

    public static synchronized PapaApacheHttpClient getInstance()
    {
        if(instance == null)
        {
            instance = new PapaApacheHttpClient();
        }

        return instance;
    }


}
