package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

/**
 * Created by shyo on 15-10-22.
 */
public class PapaDataBaseJsonError extends PapaHttpClientException {
    public PapaDataBaseJsonError()
    {
        super("数据库返回非 Json 格式喵 TwT");
    }
}

