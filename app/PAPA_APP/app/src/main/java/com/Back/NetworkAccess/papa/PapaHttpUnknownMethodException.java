package com.Back.NetworkAccess.papa;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaHttpUnknownMethodException extends PapaHttpClientException
{
    public PapaHttpUnknownMethodException()
    {
        super("Http 方法不明确喵");
    }
}
