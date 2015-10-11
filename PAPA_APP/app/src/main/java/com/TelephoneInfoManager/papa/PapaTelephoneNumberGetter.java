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
    ///
    /// zh-CN:
    /// 获取电话号码
    /// 输入 mAppContext APP 的 Context
    ///
    /// 输出 如果成功, 返回电话号码
    ///     如果失败, 返回 null
    ///
    static public String getTelephoneNumber(Context mAppContext)
    {

        // en-US: see
        // http://stackoverflow.com/questions/2480288/programmatically-obtain-the-phone-number-of-the-android-phone
        // for more details.

        TelephonyManager tMgr = (TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }
}
