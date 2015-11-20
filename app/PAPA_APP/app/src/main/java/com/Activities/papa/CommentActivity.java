package com.Activities.papa;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Back.DataBaseAccess.papa.PapaDataBaseResourceNotFound;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class CommentActivity extends AppCompatActivity {

    final static String TAG = "CommentActivity";

    BundleHelper bundleHelper;
    String course_name;
    BundleHelper.Identity identity;
    RatingBar ratingBar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_to_comment = getString(R.string.key_to_comment);
        bundleHelper = data.getParcelable(key_to_comment);
        course_name = bundleHelper.getCourseName();
        identity = bundleHelper.getIdentity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (identity == BundleHelper.Identity.teacher_assistant) {
            toolbar.setTitle(String.format(getString(R.string.view_comment),
                            bundleHelper.getStudentName(),
                            bundleHelper.getCourseName()));
        } else if (identity == BundleHelper.Identity.student) {
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
        if (identity == BundleHelper.Identity.teacher_assistant) {
            ratingBar.setEnabled(false);
            editText.setEnabled(false);
            editText.setHint(getString(R.string.no_comment));
            textView_hint_comment.setVisibility(View.GONE);
        }

        getComment();

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
        if(identity == BundleHelper.Identity.teacher_assistant)item.setVisible(false);
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
                        Float.toString(ratingBar.getRating() * 2), editText.getText().toString())
        );
    }


    private void getComment(){
        new GetCommentTask(this).execute(
                new PapaDataBaseManager.GetStudentCommentsRequest(
                        bundleHelper.getExperimentId(),
                        bundleHelper.getStudentId(),
                        bundleHelper.getToken()
                )
        );
    }


    private void afterGetComment(PapaDataBaseManager.GetStudentCommentsReply rlt)
    {
        ratingBar.setRating(Float.parseFloat(rlt.score) * 0.5f);
        Toast.makeText(getApplicationContext(), getString(R.string.hint_rating_bar_error_on_low_API_level), Toast.LENGTH_LONG).show();

        editText.setText(rlt.comments);
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

    class GetCommentTask extends
            AsyncTask<PapaDataBaseManager.GetStudentCommentsRequest,
                    Exception, PapaDataBaseManager.GetStudentCommentsReply> {
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
        protected PapaDataBaseManager.GetStudentCommentsReply doInBackground
                (PapaDataBaseManager.GetStudentCommentsRequest... params) {
            // 在后台
            try {
                return bundleHelper.getPapaDataBaseManager().getStudentComments(params[0]);
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.GetStudentCommentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null)
                afterGetComment(rlt);
        }
    }

}
