package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

public class PapaDataBaseInvalidFieldError extends PapaHttpClientException {
    public PapaDataBaseInvalidFieldError()
    {
        super("用户名或密码错误 TwT");
    }
}
