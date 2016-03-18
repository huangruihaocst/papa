package com.Back.NetworkAccess.papa;


public class PapaHttpClientUnknownParameters extends PapaHttpClientException
{
    public PapaHttpClientUnknownParameters()
    {
        super("HTTP 参数非字符串非文件, 或编码错误喵");
    }
}