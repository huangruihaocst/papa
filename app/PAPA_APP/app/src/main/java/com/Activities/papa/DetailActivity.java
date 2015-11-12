package com.Activities.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Back.DataBaseAccess.papa.PapaDataBaseResourceNotFound;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class DetailActivity extends AppCompatActivity {

    PapaDataBaseManager papaDataBaseManager;

    String experiment_name;
    BundleHelper.Identity identity;
    BundleHelper bundleHelper = new BundleHelper();
    TextView user_id;
    TextView user_class;
    EditText user_grades;
    EditText user_comment;
    TextView user_evaluator;
    boolean editable;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_to_detail = getString(R.string.key_to_detail);
        bundleHelper = data.getParcelable(key_to_detail);
        experiment_name = bundleHelper.getExperimentName();
        identity = bundleHelper.getIdentity();
        papaDataBaseManager = bundleHelper.getPapaDataBaseManager();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(bundleHelper.getStudentName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        user_id = (TextView) findViewById(R.id.user_id);
        user_class = (TextView) findViewById(R.id.user_class);
        user_grades = (EditText) findViewById(R.id.user_grade);
        user_comment = (EditText) findViewById(R.id.user_comment);
        user_evaluator = (TextView) findViewById(R.id.user_evaluator);

        fab = (FloatingActionButton) findViewById(R.id.fab_edit_detail);
        if (identity == BundleHelper.Identity.student) {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 你打算什么时候保存修改?

                if (!editable) {
                    editable = true;
                    user_grades.setEnabled(true);
                    user_grades.setFocusable(true);
                    user_comment.setEnabled(true);
                    user_comment.setFocusable(true);
                    Snackbar.make(
                            view, getString(R.string.now_editable),
                            Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_save_white_36dp));
                } else {
                    postComment();
                    Snackbar.make(
                            view, getString(R.string.now_edit_done),
                            Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit_white_36dp));
                }

            }
        });

        getComment();

        editable = false;
        user_grades.setEnabled(false);
        user_comment.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_comment) {
            Intent intent = new Intent(DetailActivity.this,CommentActivity.class);
            Bundle data = new Bundle();
            String key_to_comment = getString(R.string.key_to_comment);
            data.putParcelable(key_to_comment,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getComment(){
        new GetCommentTask(this).execute(
                new PapaDataBaseManager.GetCommentsRequest(
                        bundleHelper.getExperimentId(),
                        bundleHelper.getStudentId(),
                        bundleHelper.getToken()
                )
        );
    }

    private void setComment(PapaDataBaseManager.GetCommentsReply reply)
    {
        user_id.setText(reply.stuId);
        user_class.setText(reply.className);

        user_grades.setText(reply.score);
        user_comment.setText(reply.comments);
        user_evaluator.setText(reply.creatorName);

    }

    class GetCommentTask extends
            AsyncTask<PapaDataBaseManager.GetCommentsRequest, Exception, PapaDataBaseManager.GetCommentsReply> {
        ProgressDialog proDialog;

        public GetCommentTask(Context context) {
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
        protected PapaDataBaseManager.GetCommentsReply doInBackground
                (PapaDataBaseManager.GetCommentsRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getComments(params[0]);
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {

            // UI
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.GetCommentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setComment(rlt);
        }
    }

    private void postComment(){
        new PostCommentTask(this).execute(
                new PapaDataBaseManager.PostTACommentsRequest(
                        bundleHelper.getExperimentId(),
                        bundleHelper.getStudentId(),
                        bundleHelper.getToken(),
                        user_grades.getText().toString(),
                        user_comment.getText().toString()
                )
        );
    }

    private void afterPostComment() {
        editable = false;
        user_grades.setEnabled(false);
        user_grades.setFocusable(false);
        user_comment.setEnabled(false);
        user_comment.setFocusable(false);
    }

    class PostCommentTask extends
            AsyncTask<PapaDataBaseManager.PostTACommentsRequest, Exception, Boolean> {
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
                (PapaDataBaseManager.PostTACommentsRequest... params) {
            // 在后台
            try {
                papaDataBaseManager.postTAComments(params[0]);
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
