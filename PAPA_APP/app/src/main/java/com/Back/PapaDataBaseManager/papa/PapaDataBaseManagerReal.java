package com.Back.PapaDataBaseManager.papa;

import com.Back.DataBaseAccess.papa.PapaDataBaseAccess;
import com.Back.DataBaseAccess.papa.PapaDataBaseJsonError;
import com.Back.NetworkAccess.papa.PapaAbstractHttpClient;
import com.Back.NetworkAccess.papa.PapaHttpClientException;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by shyo on 15-10-22.
 */
public class PapaDataBaseManagerReal extends PapaDataBaseManager
{
    PapaDataBaseAccess dbAccess;

    public PapaDataBaseManagerReal()
    {
        dbAccess = new PapaDataBaseAccess();
    }

    @Override
    public SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException {
        HashMap<String, String> h = new HashMap<String, String>();

        h.put("utf8", "✓");
        h.put("user[login]", signInRequest.id);
        h.put("user[password]", signInRequest.pwd);
        h.put("user[remember_me]", "0");
        JSONObject replyObj;

        try
        {
            replyObj = dbAccess.getDataBaseReplyAsJson(PapaAbstractHttpClient.HttpMethod.post, "/users/sign_in.json", h);

            return new SignInReply(replyObj.getInt("id"), replyObj.getString("token"));
        }
        catch(org.json.JSONException e)
        {
            throw new PapaDataBaseJsonError();
        }
    }
}
