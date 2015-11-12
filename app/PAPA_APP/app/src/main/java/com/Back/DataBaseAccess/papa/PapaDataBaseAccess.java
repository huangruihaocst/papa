package com.Back.DataBaseAccess.papa;

import android.util.Log;

import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaApacheHttpClient;
import com.Back.NetworkAccess.papa.PapaHttpClientException;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaDataBaseAccess
{
    private PapaAbstractHttpClient client;

    private static final String host = "ai.m.n9.vc";
    private static final String port = "90";
    private static final String tag = "PapaDataBaseAccess";

    public PapaDataBaseAccess()
    {
        client = PapaApacheHttpClient.getInstance();
    }

    public String getDataBaseReplyAsString(PapaAbstractHttpClient.HttpMethod method, String url, HashMap<String, Object> parameters)
            throws PapaHttpClientException
    {
        url = "http://" + host + ":" + port + url;
        return client.getHttpReply(method, url, parameters);
    }

    public String getDataBaseReplyAsString(PapaAbstractHttpClient.HttpMethod method, String url)
            throws PapaHttpClientException
    {
        return getDataBaseReplyAsString(method, url, new HashMap<String, Object>());
    }

    public JSONObject getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod method, String url, HashMap<String, Object> parameters)
            throws PapaHttpClientException
    {
        String s = getDataBaseReplyAsString(method, url, parameters);

        try {
            JSONTokener jsonParser = new JSONTokener(s);
            JSONObject replyObj = (JSONObject) jsonParser.nextValue();

            if (!replyObj.getString("status").equals("successful")) {
                String reason = null;

                try
                {
                    reason = replyObj.getString("reason");
                }
                catch (org.json.JSONException e)
                {
                }

                if(reason == null)
                    throw new PapaDataBaseNotSuccessError();
                else if(reason.equals("resource_not_found"))
                    throw new PapaDataBaseResourceNotFound();
                else
                    throw new PapaDataBaseNotSuccessError(reason);
            }

            Log.i(tag, replyObj.toString());
            return replyObj;
        }
        catch(org.json.JSONException e)
        {
            throw new PapaDataBaseJsonError();
        }
    }

    public JSONObject getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod method, String url)
            throws PapaHttpClientException
    {
        return getDataBaseReplyAsJson(method, url, new HashMap<String, Object>());
    }
}
