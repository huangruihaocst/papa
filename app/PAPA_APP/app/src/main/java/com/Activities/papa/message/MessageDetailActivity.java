package com.Activities.papa.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.Activities.papa.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        title.setText(getString(R.string.string_message_title, message.getTitle()));
        type.setText(message.getType());
        message_content.setText(message.getContent());

        // fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
