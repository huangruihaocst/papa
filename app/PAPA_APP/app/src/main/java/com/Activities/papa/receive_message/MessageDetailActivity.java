package com.Activities.papa.receive_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.Activities.papa.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.util.Locale;

public class MessageDetailActivity extends AppCompatActivity {
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the message from outside
        Intent intent = getIntent();
        byte[] message_bytes = intent.getExtras().getByteArray(getString(R.string.key_message_detail_activity_message_byte_array));
        try {
            if (message_bytes == null) {
                throw new NullPointerException();
            }
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message_bytes));
            this.message = (Message) ois.readObject();

        } catch (IOException|ClassNotFoundException|NullPointerException e) {
            e.printStackTrace();
            this.message = Message.InvalidMessage;
        }

        // set content
        View content_view = findViewById(R.id.layout_activity_message_detail_content);
        TextView title = (TextView) content_view.findViewById(R.id.text_view_message_detail_title);
        TextView type = (TextView) content_view.findViewById(R.id.text_view_message_detail_type);
        TextView message_content = (TextView) content_view.findViewById(R.id.text_view_message_detail_content);
        TextView deadline = (TextView) content_view.findViewById(R.id.text_view_message_detail_deadline);
        TextView courseName = (TextView) content_view.findViewById(R.id.text_view_message_detail_course_name);
        TextView creatorName = (TextView) content_view.findViewById(R.id.text_view_message_detail_creator_name);

        title.setText(getString(R.string.string_message_title, message.getTitle()));
        type.setText(message.getType());
        message_content.setText(message.getContent());

        // deadline format
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        deadline.setText(dateFormat.format(message.getDeadline().getTime()));
        long delta = message.getDeadline().getTimeInMillis() - System.currentTimeMillis();
        if (delta > 0 && delta < this.getResources().getInteger(R.integer.min_deadline_warning_in_milliseconds)) {
            deadline.setTextColor(ContextCompat.getColor(this, R.color.color_message_nearing_deadline));
        }

        courseName.setText(message.getCourseName());
        creatorName.setText(message.getCreatorName());

//        // fab
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
