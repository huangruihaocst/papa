package com.Activities.papa;

import android.content.Intent;
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
            float rating = ratingBar.getRating();
            String comments = editText.getText().toString();
            Toast.makeText(getApplicationContext(),rating + " " + comments,Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
