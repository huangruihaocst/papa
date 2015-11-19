package com.Back.NetworkAccess.papa;

import java.util.HashMap;

public abstract class PapaAbstractHttpClient{
    protected abstract String getHttpReplyByGet(String url, HashMap<String, Object> parameter) throws PapaHttpClientException;
    protected abstract String getHttpReplyByPost(String url, HashMap<String, Object> parameter) throws PapaHttpClientException;
    protected abstract String getHttpReplyByPut(String url, HashMap<String, Object> parameter) throws PapaHttpClientException;
    protected abstract String getHttpReplyByDelete(String url, HashMap<String, Object> parameter) throws PapaHttpClientException;


    public enum HttpMethod {
        get, post, put, delete
    }

    public String getHttpReply(HttpMethod method, String url, HashMap<String, Object> parameter)
            throws PapaHttpClientException
    {
        if(method == HttpMethod.get)
            return getHttpReplyByGet(url, parameter);
        else if(method == HttpMethod.post)
            return getHttpReplyByPost(url, parameter);
        else if(method == HttpMethod.put)
            return getHttpReplyByPut(url, parameter);
        else if(method == HttpMethod.delete)
            return getHttpReplyByDelete(url, parameter);
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
