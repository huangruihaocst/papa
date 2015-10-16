package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaApacheHttpClient;
import com.Back.NetworkAccess.papa.UnknownMethodException;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

/**
 * Created by shyo on 15-10-16.
 */
public class DataBaseAccess
{
    private PapaAbstractHttpClient client;

    private static final String host = "192.168.1.115";
    private static final String port = "80";
    private static final String tag = "DataBaseAccess";

    public DataBaseAccess()
    {
        client = new PapaApacheHttpClient();
    }

    public String getDataBaseReplyAsString(PapaAbstractHttpClient.HttpMethod method, String url, HashMap<String, String> parameters)
            throws UnknownMethodException
    {
        url = "http://" + host + ":" + port + url;
        return client.getHttpReply(method, url, parameters);
    }

    public String getDataBaseReplyAsString(PapaAbstractHttpClient.HttpMethod method, String url)
            throws UnknownMethodException
    {
        url = "http://" + host + ":" + port + url;
        return client.getHttpReply(method, url);
    }

    public JSONObject getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod method, String url, HashMap<String, String> parameters)
            throws UnknownMethodException, PapaDataBaseNotSuccessError, org.json.JSONException
    {
        String s = getDataBaseReplyAsString(method, url, parameters);

        JSONTokener jsonParser = new JSONTokener(s);
        JSONObject replyObj = (JSONObject) jsonParser.nextValue();

        if(!replyObj.getString("status").equals("successful"))
        {
            throw new PapaDataBaseNotSuccessError();
        }
        return replyObj;
    }

    public JSONObject getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod method, String url)
            throws UnknownMethodException, PapaDataBaseNotSuccessError, org.json.JSONException
    {
        return getDataBaseReplyAsJson(method, url, new HashMap<String, String>());
    }
}
