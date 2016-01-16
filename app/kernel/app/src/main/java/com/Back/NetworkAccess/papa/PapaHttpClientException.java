package com.Back.NetworkAccess.papa;

/**
 * Created by shyo on 15-10-16.
 */
public abstract class PapaHttpClientException extends Exception
{
    public PapaHttpClientException() {

    }

    public PapaHttpClientException(String s)
    {
        super(s);
    }
}
