package com.Back.DataBaseAccess.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class PapaDataBaseNotSuccessError extends PapaHttpClientException {
    public String reason;

    public PapaDataBaseNotSuccessError() {
        super("数据库返回无效数据瞄 TwT");
        reason = null;
    }

    public PapaDataBaseNotSuccessError(String s) {
        super("数据库返回无效数据瞄 TwT\nげんいん：" + s);
        reason = s;
    }
}
