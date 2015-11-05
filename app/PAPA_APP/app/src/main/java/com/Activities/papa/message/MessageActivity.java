package com.Activities.papa.message;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;

public class MessageActivity extends AppCompatActivity {
    static final String TAG = "MessageActivity";

    boolean bound;
    MessagePullService messagePullService;
    MessageActivityFragment fragment;
    Menu menu;
    BundleHelper bundleHelper;

    String userId;
    String token;

    void setUserData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.w(TAG, "Null intent extra!");
            return;
        }

        bundleHelper = bundle.getParcelable(getString(R.string.key_to_message));

        if (bundleHelper != null) {
            this.userId = String.valueOf(bundleHelper.getId());
            this.token = bundleHelper.getToken();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setUserData(intent);
        flushMessages();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setUserData(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message_list);
        toolbar.setTitle(getString(R.string.message));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        bindService(new Intent(this, MessagePullService.class),
                connection,
                Context.BIND_AUTO_CREATE);
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
            messagePullService.setUserInfo(userId, token);
            bound = true;

            fragment.setMessageService(messagePullService);

            flushMessages();
            startListen();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            fragment.setMessageService(null);
            bound = false;
        }
    };

    void flushMessages() {
        new AsyncTask<Object, Exception, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Message message = new Message();
                message.what = Flush;
                message.obj = messagePullService.syncMessages();
                handler.sendMessage(message);
                return null;
            }
        }.execute();
    }

    void startListen() {
        new AsyncTask<Object, Exception, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                messagePullService.startListen();
                messagePullService.notifyMessagesNearingDeadline();
                return null;
            }
        }.execute();
    }

    static final int Flush = 1;
    static final int Init = 2;
    class MessageActivityHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Flush) {
                MessageList messageList = (MessageList) msg.obj;
                fragment.updateMessages(messageList);
            }
        }
    }
    Handler handler = new MessageActivityHandler();

    static final int MenuEditAction = 0;
    static final int MenuDeleteAction = 1;
    static final int MenuDoneAction = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.message_list, menu);
        quitEditMode(menu);
        return true;
    }

    void enterEditMode(Menu menu) {
        fragment.enterEditMode();
        menu.getItem(MenuDeleteAction).setVisible(true);
        menu.getItem(MenuDoneAction).setVisible(true);
        menu.getItem(MenuEditAction).setVisible(false);
    }
    void quitEditMode(Menu menu) {
        menu.getItem(MenuDeleteAction).setVisible(false);
        menu.getItem(MenuDoneAction).setVisible(false);
        menu.getItem(MenuEditAction).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_message_edit){
            // enter edit mode
            enterEditMode(menu);
        }
        else if (id == R.id.action_message_delete) {
            fragment.quitEditMode(true);
            quitEditMode(menu);
        }
        else if (id == R.id.action_message_done) {
            fragment.quitEditMode(false);
            quitEditMode(menu);
        }
        else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

}
