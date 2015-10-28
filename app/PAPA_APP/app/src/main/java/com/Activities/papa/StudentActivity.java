package com.Activities.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.List;
import java.util.Map;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static String tag = "StudentActivity";

    BundleHelper bundleHelper = new BundleHelper();

    private PapaDataBaseManager papaDataBaseManager;
    private int courseId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final String key_experiment_student = getString(R.string.key_experiment_student);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(key_experiment_student);
        final String experiment_name = bundleHelper.getExperimentName();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(experiment_name);
        setSupportActionBar(toolbar);

        courseId = bundleHelper.getCourseId();
        token = bundleHelper.getToken();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        papaDataBaseManager = bundleHelper.getPapaDataBaseManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(bundleHelper.getIdentity().equals("teacher_assistant")){
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_upload_history);
            item.setVisible(false);
        }
        // setHeaderView(navigationView);

        getStudents();
    }


    private void setHeaderView(NavigationView navigationView){
        LinearLayout linearLayout = (LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_course);
        TextView username_label = (TextView)linearLayout.findViewById(R.id.username_label);
        TextView mail_label = (TextView)findViewById(R.id.mail_label);
        ImageView image_label = (ImageView)findViewById(R.id.image_label);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
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
        }else if (id == R.id.action_experiment_information) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            Intent intent = new Intent(StudentActivity.this,FavoriteActivity.class);
            Bundle data = new Bundle();
            String key_to_favorite = getString(R.string.key_to_favorite);
            data.putParcelable(key_to_favorite,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_experiment_history) {
            Intent intent = new Intent(StudentActivity.this,ExperimentHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_experiment_history = getString(R.string.key_to_experiment_history);
            data.putParcelable(key_to_experiment_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_upload_history) {
            Intent intent = new Intent(StudentActivity.this,UploadHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_upload_history = getString(R.string.key_to_upload_history);
            data.putParcelable(key_to_upload_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(StudentActivity.this,EditProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(StudentActivity.this,HelpActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_help);
            data.putParcelable(key_to_help,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(StudentActivity.this,SettingsActivity.class);
            Bundle data = new Bundle();
            String key_to_settings = getString(R.string.key_to_settings);
            data.putParcelable(key_to_settings,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if(id == R.id.nav_notification){
            Intent intent = new Intent(StudentActivity.this,NotificationActivity.class);
            Bundle data = new Bundle();
            String key_to_notification = getString(R.string.key_to_notification);
            data.putParcelable(key_to_notification,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyAdapter extends BaseAdapter {
        private List<Map.Entry<Integer, String>> lst;

        public MyAdapter(List<Map.Entry<Integer, String>> lst) {
            this.lst = lst;
        }

        @Override
        public int getCount() {
            return lst.size();
        }

        @Override
        public Map.Entry<Integer, String> getItem(int position) {
            return lst.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(lst.get(position).getValue());
            mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
            mTextView.setTextColor(Color.parseColor(getString(R.string.color_primary)));
            return mTextView;
        }
    }

    /*
    private void getStudents(String experiment_name){
        student_list = new String[10];
        for(int i = 0;i < 10;i ++){
            student_list[i] = "学生" + i;
        }
    }
    */

    void getStudents()
    {
        new GetStudentsTask(this).execute(new PapaDataBaseManager.StudentsRequest(courseId, token));
    }

    void setStudents(PapaDataBaseManager.StudentsReply rlt)
    {
        final ListView StudentListView = (ListView)findViewById(R.id.student_list);
        StudentListView.setAdapter(new MyAdapter(rlt.students));
        StudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map.Entry<Integer, String> item = (Map.Entry<Integer, String>) parent.getItemAtPosition(position);
                Log.i(tag, item.getValue() + " " + item.getKey());

                Intent intent = new Intent(StudentActivity.this, DetailActivity.class);
                String key_to_detail = getString(R.string.key_to_detail);
                Bundle data = new Bundle();
                bundleHelper.setStudentId(item.getKey());
                bundleHelper.setStudentName(item.getValue());
                data.putParcelable(key_to_detail, bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    class GetStudentsTask extends
            AsyncTask<PapaDataBaseManager.StudentsRequest, Exception, PapaDataBaseManager.StudentsReply> {
        ProgressDialog proDialog;

        public GetStudentsTask(Context context) {
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
        protected PapaDataBaseManager.StudentsReply doInBackground
                (PapaDataBaseManager.StudentsRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getStudents(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.StudentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setStudents(rlt);
        }
    }


}
