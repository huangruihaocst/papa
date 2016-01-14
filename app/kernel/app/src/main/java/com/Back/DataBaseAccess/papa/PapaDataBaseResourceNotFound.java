package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

/**
 * Created by shyo on 15-10-28.
 */

public class PapaDataBaseResourceNotFound extends PapaHttpClientException {
    public PapaDataBaseResourceNotFound()
    {
        super("数据库没有数据喵 TwT");
    }
}
