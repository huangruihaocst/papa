package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

/**
 * Created by shyo on 15-10-28.
 */

public class PapaDataBaseInvalidFieldError extends PapaHttpClientException {
    public PapaDataBaseInvalidFieldError()
    {
        super("用户名或密码错误 TwT");
    }
}
