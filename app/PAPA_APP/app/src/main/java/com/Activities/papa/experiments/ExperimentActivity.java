package com.Activities.papa.experiments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.List;
import java.util.Map;

public class ExperimentActivity extends AppCompatActivity {
    final static String tag = "ExperimentActivity";

    String course_name;
    int courseId;
    BundleHelper.Identity identity;
    BundleHelper bundleHelper = new BundleHelper();

    PapaDataBaseManager papaDataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        final String key_course_experiment = getString(R.string.key_course_experiment);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(key_course_experiment);
        course_name = bundleHelper.getCourseName();
        courseId = bundleHelper.getCourseId();
        Log.i(tag, courseId + "");

        identity = bundleHelper.getIdentity();

        this.papaDataBaseManager = bundleHelper.getPapaDataBaseManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.help));
        toolbar.setTitle(course_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getExperiments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.experiment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getExperiments(){
        new Task(this).execute(new PapaDataBaseManager.LessonRequest(courseId));
    }

    class Task extends
            AsyncTask<PapaDataBaseManager.LessonRequest, Exception,PapaDataBaseManager.LessonReply> {
        ProgressDialog proDialog;

        public Task(Context context) {
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
        protected PapaDataBaseManager.LessonReply doInBackground
                (PapaDataBaseManager.LessonRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getLesson(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.LessonReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setExperiments(rlt);
        }
    }

    void setExperiments(final PapaDataBaseManager.LessonReply rlt)
    {
        final ListView ExperimentListView = (ListView)findViewById(R.id.experiment_list);
        ExperimentListView.setAdapter(new ExperimentsListAdapter(rlt.lesson,getApplicationContext()));
        ExperimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExperimentActivity.this,ExperimentDetailActivity.class);
                Bundle data = new Bundle();
                String key_experiment_experiment_detail = getString(R.string.key_experiment_experiment_detail);
                data.putParcelable(key_experiment_experiment_detail,bundleHelper);

                Map.Entry<Integer, String> item = (Map.Entry<Integer, String>)parent.getItemAtPosition(position);

                bundleHelper.setExperimentName(item.getValue());
                bundleHelper.setExperiment_id(item.getKey());

                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

}
