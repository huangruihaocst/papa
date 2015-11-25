package com.Activities.papa.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SentDetailActivity extends AppCompatActivity {

    BundleHelper bundleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_detail_activity);

        String key_sent_list_sent_detail = getString(R.string.key_sent_list_sent_detail);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(key_sent_list_sent_detail);
        PapaDataBaseManager.ChatMessage chatMessage = bundleHelper.getChatMessage();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(String.format(getString(R.string.message_from), chatMessage.senderName));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView sender = (TextView)findViewById(R.id.user_id);
        TextView sent_time = (TextView)findViewById(R.id.user_grades);
        TextView title = (TextView)findViewById(R.id.title);
        TextView body = (TextView)findViewById(R.id.body);

        sender.setText(chatMessage.senderName);
        title.setText(chatMessage.title);
        body.setText(chatMessage.content);

        Calendar send_time_calendar = chatMessage.created_at;
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(String.format(getString(R.string.time_format_full), "yyyy", "MM", "dd") + " HH:mm",
                        Locale.CHINA);
        simpleDateFormat.setTimeZone(send_time_calendar.getTimeZone());
        String time = simpleDateFormat.format(send_time_calendar.getTime());
        sent_time.setText(time);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
