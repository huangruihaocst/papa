package com.Back.PapaDataBaseManager.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

public class PapaDataBaseTeacherError extends PapaHttpClientException {
    public PapaDataBaseTeacherError()
    {
        super("老师请从网页端登录喵 TwT");
    }
}