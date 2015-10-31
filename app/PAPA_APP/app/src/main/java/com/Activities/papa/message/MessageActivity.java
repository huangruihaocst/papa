package com.Activities.papa.message;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.Activities.papa.R;

public class MessageActivity extends AppCompatActivity {
    boolean bound;
    MessagePullService messagePullService;
    MessageActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message_list);
        setSupportActionBar(toolbar);

        fragment = (MessageActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_message_content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_message_clear_all);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(bound) {
                    messagePullService.clearMessageCache();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MessagePullService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bound)
            unbindService(connection);
    }



    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MessagePullService.LocalBinder binder = (MessagePullService.LocalBinder) service;
            messagePullService = binder.getService();
            bound = true;

            MessageList messageList = messagePullService.syncMessages();
            Toast.makeText(MessageActivity.this, "Total messages: " + String.valueOf(messageList.size()), Toast.LENGTH_SHORT).show();
            fragment.updateMessages(messageList);

            messagePullService.startListen();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

}
