package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

public class PapaDataBaseResourceNotFound extends PapaHttpClientException {
    public PapaDataBaseResourceNotFound()
    {
        super("数据库没有数据喵 TwT");
    }
}
