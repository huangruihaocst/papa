package com.Back.NetworkAccess.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.Back.NetworkAccess.papa.PapaApacheHttpClient;

import java.util.HashMap;

/**
 * Created by shyo on 15-10-16.
 */
public class ApacheTest extends InstrumentationTestCase
{
    public void testHttp1() throws Exception {
        String s = PapaApacheHttpClient.getInstance().getHttpReply(PapaApacheHttpClient.HttpMethod.get, "http://www.baidu.com", new HashMap<String, Object>());

        assertFalse(s == null || s == "");

        Log.i("testApacheGet", s);

        s = PapaApacheHttpClient.getInstance().getHttpReply(PapaApacheHttpClient.HttpMethod.get, "http://www.baidu.com", null);

        assertFalse(s == null || s == "");

        Log.i("testApacheGet", s);
    }


    public void testHttp2() throws Exception {
        String s = PapaApacheHttpClient.getInstance().getHttpReply(PapaApacheHttpClient.HttpMethod.get, "http://shyoshyo.ddns.net:8081/index.html", null);

        assertFalse(s == null || s == "");

        Log.i("testApacheGet", s);
    }



    public void testHttp3() throws Exception {
        String s = PapaApacheHttpClient.getInstance().getHttpReply(PapaApacheHttpClient.HttpMethod.get, "http://info.tsinghua.edu.cn", new HashMap<String, Object>());

        assertFalse(s == null || s == "");

        Log.i("testApacheGet", s);
    }
}