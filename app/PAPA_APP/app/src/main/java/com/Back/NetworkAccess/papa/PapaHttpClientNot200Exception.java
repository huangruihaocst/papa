package com.Back.NetworkAccess.papa;

/**
 * Created by shyo on 15-10-22.
 */
public class PapaHttpClientNot200Exception extends PapaHttpClientException
{
    public PapaHttpClientNot200Exception(int code)
    {
        super("HTTP 返回 " + code + " 错误");
    }
}
