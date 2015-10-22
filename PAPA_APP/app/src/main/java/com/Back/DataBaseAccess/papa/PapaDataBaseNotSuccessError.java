package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaDataBaseNotSuccessError extends PapaHttpClientException {
    public PapaDataBaseNotSuccessError()
    {
        super("数据库返回无效数据瞄 TwT");
    }
}
