package com.Back.NetworkAccess.papa;

import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class PapaApacheHttpClient extends PapaAbstractHttpClient
{
    final static String TAG = "PapaApacheHttpClient";

    @NotThreadSafe
    class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";
        public String getMethod() { return METHOD_NAME; }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() { super(); }
    }

    // 执行 get / post 方法
    private static HttpResponse executeRequest(HttpRequestBase request)
            throws PapaHttpClientException
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 3 * 1000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        try {
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return response;
            } else {
                Log.e(TAG, "HTTP " + response.getStatusLine().getStatusCode() + ", NOT 200 OK");
                throw new PapaHttpClientNot200Exception(response.getStatusLine().getStatusCode());
            }
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
            throw new PapaHttpClientIOErrorException();
        }
    }

    private static String executeRequestAsString(HttpRequestBase request)
        throws PapaHttpClientException
    {
        try {
            HttpResponse response = executeRequest(request);
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            return content;
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
            throw new PapaHttpClientIOErrorException();
        }
    }

    private String getFullUrl(String url, HashMap<String, Object> parameter)
    {
        // 准备 url
        if(parameter != null && !parameter.isEmpty())
        {
            url += '?';

            boolean first = true;

            Iterator it = parameter.keySet().iterator();
            while(it.hasNext()) {
                String key = (String) it.next();

                if (parameter.get(key) instanceof String) {
                    url += key + '=' + parameter.get(key);
                    if (first)
                        first = false;
                    else
                        url += '&';
                }
            }
        }

        return url;
    }

    @Override
    protected void getHttpReplyByGet(String url, HashMap<String, Object> parameter, File file) throws PapaHttpClientException {
        try
        {
            OutputStream outputStream = new FileOutputStream(file);
            url = getFullUrl(url, parameter);
            HttpGet get = new HttpGet(url);
            executeRequest(get).getEntity().writeTo(outputStream);
            outputStream.close();
        }
        catch(java.io.IOException e)
        {
            e.printStackTrace();
            throw new PapaHttpClientIOErrorException();
        }
    }

    @Override
    protected String getHttpReplyByGet(String url, HashMap<String, Object> parameter)
        throws PapaHttpClientException
    {
        url = getFullUrl(url, parameter);

        HttpGet get = new HttpGet(url);

        Log.i(TAG, "url = " + get.getURI().toString());

        // 执行请求
        String reply = null;

        Log.i(TAG, "ret = " + (reply = executeRequestAsString(get)));


        Log.i(TAG, reply.toString());

        return reply;
    }

    private String getHttpReply(
            String url, HashMap<String, Object> parameter,
            HttpEntityEnclosingRequestBase post
    )
        throws PapaHttpClientException
    {
        try {
            post.setURI(new URI(url));
        }
        catch(URISyntaxException e) {
            e.printStackTrace();

            throw new PapaHttpClientIOErrorException();
        }

        Log.i(TAG, "post = " + post.getURI().toString() + ", postData ");

        // See
        MultipartEntity mpEntity = new MultipartEntity();
        if(!parameter.isEmpty())
        {
            Iterator it = parameter.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                Object val = parameter.get(key);
                if (val instanceof String)
                {
                    try
                    {
                        mpEntity.addPart(key, new StringBody((String) val, Charset.forName("UTF-8")));
                        Log.i(TAG, key + " = " + val);
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        throw new PapaHttpClientUnknownParameters();
                    }
                }
                else if (val instanceof File)
                {


                    mpEntity.addPart(key, new FileBody((File)val));
                }
                else
                    throw new PapaHttpClientUnknownParameters();
            }
        }


        post.setEntity(mpEntity);

        // 执行请求
        String reply = null;
        Log.i(TAG, "ret = " + (reply = executeRequestAsString(post)));
        Log.i(TAG, reply.toString());
        return reply;
    }

    protected String getHttpReplyByPost(String url, HashMap<String, Object> parameter)
            throws PapaHttpClientException
    {
        return getHttpReply(url, parameter, new HttpPost());
    }


    protected String getHttpReplyByPut(String url, HashMap<String, Object> parameter)
            throws PapaHttpClientException
    {
        return getHttpReply(url, parameter, new HttpPut());
    }

    protected String getHttpReplyByDelete(String url, HashMap<String, Object> parameter)
            throws PapaHttpClientException
    {
        return getHttpReply(url, parameter, new HttpDeleteWithBody());
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
