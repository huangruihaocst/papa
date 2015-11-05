package com.Back.NetworkAccess.papa;

/**
 * Created by shyo on 15-11-5.
 */
public class PapaHttpClientUnknownParameters extends PapaHttpClientException
{
    public PapaHttpClientUnknownParameters()
    {
        super("HTTP 参数非字符串非文件, 或编码错误喵");
    }
}