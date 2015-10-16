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
    public static class cannotGetTelephoneNumberException extends Exception
    {
        public cannotGetTelephoneNumberException()
        {

        }
    }

    ///
    /// zh-CN:
    /// 获取电话号码
    /// 输入 mAppContext APP 的 Context
    ///
    /// 输出 如果成功, 返回电话号码
    ///     如果失败, 返回 null
    ///
    static public String getTelephoneNumber(Context mAppContext) throws cannotGetTelephoneNumberException
    {
        // en-US: see
        // http://stackoverflow.com/questions/2480288/programmatically-obtain-the-phone-number-of-the-android-phone
        // for more details.

        TelephonyManager tMgr = (TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        // zh-CN: 有可能返回空串
        if(mPhoneNumber != null && mPhoneNumber.equals("")) mPhoneNumber = null;

        if(mPhoneNumber == null) throw new cannotGetTelephoneNumberException();

        return mPhoneNumber;
    }
}
