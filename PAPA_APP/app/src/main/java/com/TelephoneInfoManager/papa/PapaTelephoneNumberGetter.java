/**
 * Created by shyo on 15-10-11.
 *
 * zh-CN: 获取电话号码
 * ja-JP: 電話番号を取得する
 */

package com.TelephoneInfoManager.papa;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PapaTelephoneNumberGetter
{
    // 获取不了手机号的异常
    public class cannotGetTelephoneNumberException extends Exception
    {
        public cannotGetTelephoneNumberException()
        {

        }
    }


    public String getTelephoneNumber(Context mAppContext) throws cannotGetTelephoneNumberException
    {
        throw new cannotGetTelephoneNumberException();
    }
}