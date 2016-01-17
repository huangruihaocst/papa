package com.Back.NetworkAccess.papa;

public class PapaHttpClientIOErrorException extends PapaHttpClientException
{
    public PapaHttpClientIOErrorException()
    {
        super("网络 IO 读写错误");
    }
}