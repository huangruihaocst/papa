package com.Back.PapaDataBaseManager.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;

/**
 * Created by shyo on 15-10-28.
 */
public class PapaDataBaseTeacherError extends PapaHttpClientException {
    public PapaDataBaseTeacherError()
    {
        super("管理员请从网页端登录喵 TwT");
    }
}