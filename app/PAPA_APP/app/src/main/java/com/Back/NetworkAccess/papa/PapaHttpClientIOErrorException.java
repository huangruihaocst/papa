package com.Back.NetworkAccess.papa;

/**
 * Created by shyo on 15-10-22.
 */

public class PapaHttpClientIOErrorException extends PapaHttpClientException
{
    public PapaHttpClientIOErrorException()
    {
        super("网络 IO 读写错误");
    }
}