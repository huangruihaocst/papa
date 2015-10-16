package com.Activities.papa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetter;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetterReal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class EnterActivity extends AppCompatActivity {

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
                String key_enter_sign_in = getString(R.string.key_enter_sign_in);
                data.putParcelable(key_enter_sign_in, bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        };
        timer.schedule(timerTask,1000);
    }
}
