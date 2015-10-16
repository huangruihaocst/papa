package com.TelephoneInfoManager.papa;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by shyo on 15-10-16.
 */
public class PapaTelephoneNumberGetterReal extends PapaTelephoneNumberGetter
{
    ///
    /// zh-CN:
    /// 获取电话号码
    /// 输入 mAppContext APP 的 Context
    ///
    /// 输出 如果成功, 返回电话号码
    ///     如果失败, 返回 null
    ///
    public String getTelephoneNumber(Context mAppContext) throws cannotGetTelephoneNumberException
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
