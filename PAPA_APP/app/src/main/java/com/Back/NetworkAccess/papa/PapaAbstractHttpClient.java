package com.Back.NetworkAccess.papa;

import java.util.HashMap;

public abstract class PapaAbstractHttpClient {
    public abstract String getHttpReplyByGet(String url, HashMap<String, String> parameter);
    public abstract String getHttpReplyByPost(String url, HashMap<String, String> parameter);

    public enum HttpMethod {
        get, post
    }



    public String getHttpReply(HttpMethod method, String url, HashMap<String, String> parameter)
            throws UnknownMethodException
    {
        if(method == HttpMethod.get)
            return getHttpReplyByGet(url, parameter);
        else if(method == HttpMethod.post)
            return getHttpReplyByPost(url, parameter);
        else
            throw new UnknownMethodException();
    }

    public String getHttpReply(HttpMethod method, String url)
            throws UnknownMethodException
    {
        if(method == HttpMethod.get)
            return getHttpReplyByGet(url, new HashMap<String, String>());
        else if(method == HttpMethod.post)
            return getHttpReplyByPost(url, new HashMap<String, String>());
        else
            throw new UnknownMethodException();
    }
}
