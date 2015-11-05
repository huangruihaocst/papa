package com.Activities.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Back.DataBaseAccess.papa.PapaDataBaseResourceNotFound;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class CommentActivity extends AppCompatActivity {

    BundleHelper bundleHelper;
    String course_name;
    String identity;
    RatingBar ratingBar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_experiment_detail_comment = getString(R.string.key_experiment_detail_comment);
        bundleHelper = data.getParcelable(key_experiment_detail_comment);
        course_name = bundleHelper.getCourseName();
        identity = bundleHelper.getIdentity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (identity.equals("teacher_assistant")) {
            toolbar.setTitle(getString(R.string.view_comment,bundleHelper.getCourseName()));
        } else if (identity.equals("student")) {
            toolbar.setTitle(getString(R.string.for_course) + course_name);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView_hint_comment = (TextView)findViewById(R.id.hint_comment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editText = (EditText) findViewById(R.id.editText);
        if (identity.equals("teacher_assistant")) {
            ratingBar.setEnabled(false);
            editText.setEnabled(false);
            editText.setHint(getString(R.string.no_comment));
            textView_hint_comment.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.comment, menu);
        MenuItem item = menu.getItem(0);
        if(identity.equals("teacher_assistant"))item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // float rating = ratingBar.getRating();
            // String comments = editText.getText().toString();
            // Toast.makeText(getApplicationContext(),rating + " " + comments,Toast.LENGTH_LONG).show();
            postComment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postComment(){
        new PostCommentTask(this).execute(
                new PapaDataBaseManager.PostStudentCommentsRequest(
                        bundleHelper.getExperimentId(),
                        bundleHelper.getStudentId(),
                        bundleHelper.getToken(),
                        Float.toString(ratingBar.getRating()),
                        editText.getText().toString()
                )
        );
    }

    private void afterPostComment() {
        Toast.makeText(getApplicationContext(), "发送了喵", Toast.LENGTH_LONG).show();
    }

    class PostCommentTask extends
            AsyncTask<PapaDataBaseManager.PostStudentCommentsRequest, Exception, Boolean> {
        ProgressDialog proDialog;

        public PostCommentTask(Context context) {
            proDialog = new ProgressDialog(context, 0);
            proDialog.setMessage("稍等喵 =w=");
            proDialog.setCancelable(false);
            proDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            // UI

            proDialog.show();
        }

        @Override
        protected Boolean doInBackground
                (PapaDataBaseManager.PostStudentCommentsRequest... params) {
            // 在后台
            try {
                bundleHelper.getPapaDataBaseManager().postStudentComments(params[0]);
                return true;
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {

            if(e[0] instanceof PapaDataBaseResourceNotFound)
                onBackPressed();

            // if(e)
            // UI
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean rlt) {
            // UI

            proDialog.dismiss();
            if (rlt)
                afterPostComment();
        }
    }

}
