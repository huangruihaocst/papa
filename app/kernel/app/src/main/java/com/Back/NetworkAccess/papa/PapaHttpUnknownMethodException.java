package com.Back.NetworkAccess.papa;

public class PapaHttpUnknownMethodException extends PapaHttpClientException
{
    public PapaHttpUnknownMethodException()
    {
        super("Http 方法不明确喵");
    }
}
