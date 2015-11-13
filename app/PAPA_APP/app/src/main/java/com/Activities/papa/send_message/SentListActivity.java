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
import android.widget.ListView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class SentListActivity extends AppCompatActivity {

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
        toolbar.setTitle(getString(R.string.send_message));
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
        new Task(this).execute(
                new PapaDataBaseManager.GetChatMessageRequest(bundleHelper.getToken())
        );
    }

    class Task extends AsyncTask<
            PapaDataBaseManager.GetChatMessageRequest,
            Exception,
            PapaDataBaseManager.GetChatMessageReply>
    {
        ProgressDialog proDialog;

        public Task(Context context) {
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

    private void processReply(PapaDataBaseManager.GetChatMessageReply reply)
    {
        String s = "";
        for(int i = 0; i < reply.list.size(); i++)
        {
            PapaDataBaseManager.ChatMessage chatMessage =
                reply.list.get(i);

            s += "id = " + chatMessage.id;
            s += ", senderId = " + chatMessage.senderId;
            s += ", senderName = " + chatMessage.senderName;
            s += ", title = " + chatMessage.title;
            s += ", content = " + chatMessage.content;
            s += ", status = " + chatMessage.status;
            s += "\n";

        }


        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
