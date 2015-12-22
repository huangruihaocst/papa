package com.Activities.papa.receive_message;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Settings.Settings;

public class MessageActivity extends AppCompatActivity {
    static final String TAG = "MessageActivity";

    boolean bound;
    MessagePullService messagePullService;

    Menu menu;
    ListView messageListView;
    MessageListAdapter adapter;
    SwipeRefreshLayout layout;

    String userId;
    String token;

    void setUserData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.w(TAG, "Null intent extra!");
            return;
        }

        BundleHelper bundleHelper = bundle.getParcelable(getString(R.string.key_to_notification));

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

        // id and token
        setUserData(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message_list);
        toolbar.setTitle(getString(R.string.notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * SwipeRefreshLayout
         * */
        layout = (SwipeRefreshLayout) findViewById(R.id.layout_content_message_list);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flushMessages();
            }
        });
        layout.setColorSchemeColors(ContextCompat.getColor(MessageActivity.this, R.color.colorPrimary));

        /**
         * ListView
         */
        messageListView = (ListView) layout.findViewById(R.id.listViewMessageList);
        adapter = new MessageListAdapter(MessageActivity.this, new MessageList());
        messageListView.setAdapter(adapter);

        /**
         * Floating Action Button
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_message_clear_all);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(bound) {
//                    Settings.clearCache(MessageActivity.this);
//                            flushMessages();
//                }
                adapter.selectAll();
                adapter.quitEditMode(true);
                quitEditMode(menu);
                Snackbar.make(view, getString(R.string.notification_clear_all), Snackbar.LENGTH_LONG).show();
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
            adapter.setMessageService(messagePullService);

            flushMessages();
            startListen();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
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
                adapter.resetData(messageList);
                layout.setRefreshing(false);
            }
        }
    }
    Handler handler = new MessageActivityHandler();

    static final int MenuEditAction = 0;
    static final int MenuDeleteAction = 1;
    static final int MenuDoneAction = 2;
    static final int MenuSelectUnselectedAll = 3;
    static final int MenuMarkAllAsRead = 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.message_list, menu);
        quitEditMode(menu);
        return true;
    }

    void enterEditMode(Menu menu) {
        adapter.enterEditMode();
        menu.getItem(MenuDeleteAction).setVisible(true);
        menu.getItem(MenuDoneAction).setVisible(true);
        menu.getItem(MenuEditAction).setVisible(false);
        menu.getItem(MenuSelectUnselectedAll).setVisible(true);
        menu.getItem(MenuSelectUnselectedAll).setTitle(getString(R.string.select_all));
    }
    void quitEditMode(Menu menu) {
        menu.getItem(MenuDeleteAction).setVisible(false);
        menu.getItem(MenuDoneAction).setVisible(false);
        menu.getItem(MenuEditAction).setVisible(true);
        menu.getItem(MenuSelectUnselectedAll).setVisible(false);
        menu.getItem(MenuSelectUnselectedAll).setTitle(getString(R.string.reverse_select));
    }
    boolean selectAllMode = true;
    void toggleSelectAll() {
        if (selectAllMode) {
            menu.getItem(MenuSelectUnselectedAll).setTitle(getString(R.string.reverse_select));
            adapter.selectAll();
        } else {
            menu.getItem(MenuSelectUnselectedAll).setTitle(getString(R.string.select_all));
            adapter.reverseSelect();
        }
        selectAllMode = !selectAllMode;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_message_edit){
            // enter edit mode
            enterEditMode(menu);
        }
        else if (id == R.id.action_message_delete) {
            adapter.quitEditMode(true);
            quitEditMode(menu);
        }
        else if (id == R.id.action_message_done) {
            adapter.quitEditMode(false);
            quitEditMode(menu);
        }
        else if (id == R.id.action_message_mark_all_as_read) {
            adapter.markAllAsRead();
        }
        else if (id == R.id.action_message_select_unselected_all) {
            toggleSelectAll();
        }
        else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

}
