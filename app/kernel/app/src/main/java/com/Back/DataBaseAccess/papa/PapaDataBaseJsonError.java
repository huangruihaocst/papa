package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

public class PapaDataBaseJsonError extends PapaHttpClientException {
    public PapaDataBaseJsonError()
    {
        super("数据库返回非 Json 格式喵 TwT");
    }
}

