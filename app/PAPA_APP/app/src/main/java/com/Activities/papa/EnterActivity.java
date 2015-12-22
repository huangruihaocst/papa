package com.Activities.papa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.Back.TelephoneInfoManager.papa.PapaTelephoneNumberGetter;
import com.Back.TelephoneInfoManager.papa.PapaTelephoneNumberGetterReal;
import com.Helpers.BundleHelper;

import java.util.Timer;
import java.util.TimerTask;

public class EnterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(EnterActivity.this,SignInActivity.class);
                BundleHelper bundleHelper = new BundleHelper();
                Bundle data = new Bundle();
                PapaTelephoneNumberGetter papaTelephoneNumberGetterReal = new PapaTelephoneNumberGetterReal();
//                bundleHelper.setPapaTelephoneNumberGetter(papaTelephoneNumberGetterReal);
                String key_enter_sign_in = getString(R.string.key_to_sign_in);
                data.putParcelable(key_enter_sign_in, bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        };
        timer.schedule(timerTask, 1000);
    }
}
