package com.Back.NetworkAccess.papa;

import java.util.HashMap;

public abstract class PapaAbstractHttpClient{
    protected abstract String getHttpReplyByGet(String url, HashMap<String, String> parameter) throws PapaHttpClientException;
    protected abstract String getHttpReplyByPost(String url, HashMap<String, String> parameter) throws PapaHttpClientException;

    public enum HttpMethod {
        get, post
    }

    public String getHttpReply(HttpMethod method, String url, HashMap<String, String> parameter)
            throws PapaHttpClientException
    {
        if(method == HttpMethod.get)
            return getHttpReplyByGet(url, parameter);
        else if(method == HttpMethod.post)
            return getHttpReplyByPost(url, parameter);
        else
            throw new PapaHttpUnknownMethodException();
    }

    /*
    public String getHttpReply(HttpMethod method, String url)
            throws PapaHttpUnknownMethodException
    {
        return getHttpReply(method, url, new HashMap<String, String>());
    }
    */

    // オネエサン欲しいいいいいいいいいいいいいいい！
    // @ 打断腿送 ドイツ 的翔翔
}
