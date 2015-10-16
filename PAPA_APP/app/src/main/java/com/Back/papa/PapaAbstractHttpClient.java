package com.Back.papa;

import java.util.Dictionary;

/**
 * Created by shyo on 15-10-15.
 */

public abstract class PapaAbstractHttpClient {
    abstract String getHttpReplyByGet(String url, Dictionary<String, String> parameter);
    abstract String getHttpReplyByPost(String url, Dictionary<String, String> parameter);

    public enum HttpMethod {
        get, post
    }

    public class UnknownMethodException extends Exception
    {
    }

    String getHttpReply(HttpMethod method, String url, Dictionary<String, String> parameter)
            throws UnknownMethodException
    {
        if(method == HttpMethod.get)
            return getHttpReplyByGet(url, parameter);
        else if(method == HttpMethod.post)
            return getHttpReplyByPost(url, parameter);
        else
            throw new UnknownMethodException();
    }
}
