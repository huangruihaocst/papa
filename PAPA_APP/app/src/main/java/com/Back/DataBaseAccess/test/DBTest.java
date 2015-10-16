package com.Back.DataBaseAccess.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.Back.DataBaseAccess.papa.DataBaseAccess;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by shyo on 15-10-16.
 */
public class DBTest extends InstrumentationTestCase
{
    public void test1() throws Exception {
        DataBaseAccess dbAccess = new DataBaseAccess();

        JSONObject j = dbAccess.getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod.get, "/semesters.json");

        assertFalse(j == null);

        Log.i("testApacheGet", j.toString());
    }

    public void test2() throws Exception {
        DataBaseAccess dbAccess = new DataBaseAccess();

        HashMap<String, String> h = new HashMap<String, String>();

        h.put("utf8", "✓");
        h.put("user[login]", "123");
        h.put("user[password]", "123");
        h.put("user[remember_me]", "0");

        JSONObject j = dbAccess.getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod.post, "/users/sign_in.json", h);

        assertFalse(j == null);

        Log.i("testApacheGet", j.toString());
    }
}
