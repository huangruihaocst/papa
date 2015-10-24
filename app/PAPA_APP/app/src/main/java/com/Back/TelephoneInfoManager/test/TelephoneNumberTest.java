package com.Back.TelephoneInfoManager.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.Back.TelephoneInfoManager.papa.PapaTelephoneNumberGetterReal;

import android.util.Log;

/**
 * Created by shyo on 15-10-11.
 *
 * ja-JP: 電話番号取得の単体テスト
 * zh-CN: 获取电话号码的单元测试
 */

public class TelephoneNumberTest extends InstrumentationTestCase {
    public void test() throws Exception {
        Context context = getInstrumentation().getContext();
        String s = new PapaTelephoneNumberGetterReal().getTelephoneNumber(context);

        if(s == null)
        {
            Log.i("TelephoneNumberTest", "zh-CN: 无法获得电话 \n ja-JP: 携帯電話の番号は取得できません。");
            return;
        }
        else
        {
            Log.i("TelephoneNumberTest", "zh-CN: 电话是 " + s + "\n ja-JP: 番号は " + s);
        }


        // 翔翔才不会告诉你电话呢瞄
        assertEquals(s.charAt(s.length()-1), '4');
    }
}