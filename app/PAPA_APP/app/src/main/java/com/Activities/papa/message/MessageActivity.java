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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.Activities.papa.R;

public class MessageActivity extends AppCompatActivity {
    boolean bound;
    MessagePullService messagePullService;
    MessageActivityFragment fragment;
    Menu menu;

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
                if(bound) {
                    messagePullService.clearMessageCache();
                    flushMessages();
                }
                Snackbar.make(view, "Cache Cleared", Snackbar.LENGTH_LONG).show();
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

            fragment.setMessageService(messagePullService);

            flushMessages();

            messagePullService.startListen();
            messagePullService.notifyMessagesNearingDeadline();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            fragment.setMessageService(null);
            bound = false;
        }
    };

    void flushMessages() {
        fragment.updateMessages(messagePullService.syncMessages());
    }

    static final int MenuEditAction = 0;
    static final int MenuDeleteAction = 1;
    static final int MenuDoneAction = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.message_list, menu);
        menu.getItem(MenuDeleteAction).setVisible(false);
        menu.getItem(MenuDoneAction).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_message_edit){
            // enter edit mode
            fragment.enterEditMode();
            menu.getItem(MenuDeleteAction).setVisible(true);
            menu.getItem(MenuDoneAction).setVisible(true);
            menu.getItem(MenuEditAction).setVisible(false);
            return true;
        }
        else if (id == R.id.action_message_delete) {
            // quit edit mode
            fragment.quitEditMode(true);
            menu.getItem(MenuDeleteAction).setVisible(false);
            menu.getItem(MenuDoneAction).setVisible(false);
            menu.getItem(MenuEditAction).setVisible(true);
            return true;
        }
        else if (id == R.id.action_message_done) {
            // quit edit mode
            fragment.quitEditMode(false);
            menu.getItem(MenuDeleteAction).setVisible(false);
            menu.getItem(MenuDoneAction).setVisible(false);
            menu.getItem(MenuEditAction).setVisible(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
