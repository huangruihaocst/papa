package com.Activities.papa.send_message;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class SentListActivity extends AppCompatActivity {
    static public String TAG = "SentListActivity";

    ListView list_sent;
    BundleHelper bundleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_list);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_to_send_message = getString(R.string.key_to_sent_list);
        bundleHelper = data.getParcelable(key_to_send_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.send_message_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_sent = (ListView)findViewById(R.id.list_sent);
        setSentMessages();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentListActivity.this,NewMessageActivity.class);
                Bundle data = new Bundle();
                String key_sent_list_new_message = getString(R.string.key_sent_list_new_message);
                data.putParcelable(key_sent_list_new_message,bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    private void setSentMessages(){
        new GetMessagesTask(this).execute(
                new PapaDataBaseManager.GetChatMessageRequest(bundleHelper.getToken())
        );
    }

    class GetMessagesTask extends AsyncTask<
            PapaDataBaseManager.GetChatMessageRequest,
            Exception,
            PapaDataBaseManager.GetChatMessageReply>
    {
        ProgressDialog proDialog;

        public GetMessagesTask(Context context) {
            proDialog = new ProgressDialog(context, 0);
            proDialog.setMessage("稍等喵 =w=");
            proDialog.show();
            proDialog.setCancelable(false);
            proDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute(){
            // UI
        }

        @Override
        protected PapaDataBaseManager.GetChatMessageReply doInBackground
                (PapaDataBaseManager.GetChatMessageRequest... params)
        {
            // 在后台
            try {
                return bundleHelper.getPapaDataBaseManager().getChatMessages(params[0]);
            } catch(PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            Log.e("SignInAct", e[0].getMessage());
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.GetChatMessageReply rlt) {
            // UI

            proDialog.dismiss();
            if(rlt != null) processReply(rlt);
        }
    }

    class ReadMessagesTask extends AsyncTask<
            PapaDataBaseManager.ReadChatMessageRequest,
            Exception,
            Boolean> {
        ProgressDialog proDialog;

        public ReadMessagesTask(Context context) {
            proDialog = new ProgressDialog(context, 0);
            proDialog.setMessage("稍等喵 =w=");
            proDialog.show();
            proDialog.setCancelable(false);
            proDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute(){
            // UI
        }

        @Override
        protected Boolean doInBackground
                (PapaDataBaseManager.ReadChatMessageRequest... params)
        {
            // 在后台
            try {
                bundleHelper.getPapaDataBaseManager().readChatMessages(params[0]);

                return true;
            } catch(PapaHttpClientException e) {
                publishProgress(e);
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            Log.e("SignInAct", e[0].getMessage());
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            // UI

            proDialog.dismiss();
            if(ok) Log.i(TAG, "已设置为已读");
        }
    }

    private void processReply(final PapaDataBaseManager.GetChatMessageReply reply) {
        ////////////////////开始读最后一条信息
        /*
        new ReadMessagesTask(this).execute(new PapaDataBaseManager.ReadChatMessageRequest(
                        bundleHelper.getToken(),
                        reply.list.get(reply.list.size() - 1).id
            )
        );
        */
        ////////////////////读完最后一条信息

        list_sent.setAdapter(new SentListAdapter(reply.list, getApplicationContext()));
        list_sent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PapaDataBaseManager.ChatMessage chatMessage = reply.list.get(position);
                Intent intent = new Intent(SentListActivity.this, SentDetailActivity.class);
                bundleHelper.setChatMessage(chatMessage);
                Bundle data = new Bundle();
                String key_sent_list_sent_detail = getString(R.string.key_sent_list_sent_detail);
                data.putParcelable(key_sent_list_sent_detail, bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
}
