package com.Activities.papa.send_message;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class NewMessageActivity extends AppCompatActivity {

    EditText edit_title;
    EditText edit_body;
    EditText edit_recipient;

    BundleHelper bundleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.new_message));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String key_sent_list_new_message = getString(R.string.key_sent_list_new_message);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(key_sent_list_new_message);

        edit_title = (EditText)findViewById(R.id.content_title);
        edit_body = (EditText)findViewById(R.id.content_body);
        edit_recipient = (EditText)findViewById(R.id.content_recipient);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_message) {
            send();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void send(){
        String title = edit_title.getText().toString();
        String body = edit_body.getText().toString();
        String recipient = edit_recipient.getText().toString();

        new Task(this).execute(new PapaDataBaseManager.PostChatMessageRequest(
                bundleHelper.getToken(), title, body, "3"));
    }

    class Task extends AsyncTask<
            PapaDataBaseManager.PostChatMessageRequest,
            Exception,
            Boolean>
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
        protected Boolean doInBackground
                (PapaDataBaseManager.PostChatMessageRequest... params)
        {
            // 在后台
            try {
                bundleHelper.getPapaDataBaseManager().postChatMessages(params[0]);

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
            if(ok)
                successAndReturn();
        }
    }

    private void successAndReturn()
    {
        finish();
        Toast.makeText(getApplicationContext(), "发送成功 =w=", Toast.LENGTH_SHORT).show();
    }
}
