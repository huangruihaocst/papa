package com.Back.PapaDataBaseManager.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;


public class PapaDataBaseAdminError extends PapaHttpClientException {
    public PapaDataBaseAdminError()
    {
        super("管理员请从网页端登录喵 TwT");
    }
}