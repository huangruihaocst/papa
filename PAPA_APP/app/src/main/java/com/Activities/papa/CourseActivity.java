package com.Activities.papa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] course_teacher_assistant_list;
    private String[] course_student_list;
    BundleHelper bundleHelper = new BundleHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_sign_in_course = getString(R.string.key_sign_in_course);
        bundleHelper = data.getParcelable(key_sign_in_course);
//        String key_semester_course_1 = getString(R.string.key_semester_course_1);
//        String semester_name = data.getString(key_semester_course_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.hint_select_course));
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getCourses();

        ListView CourseTeacherAssistantListView = (ListView)findViewById(R.id.course_teacher_assistant_list);
        CourseTeacherAssistantListView.setAdapter(new MyTeacherAssistantAdapter());
        CourseTeacherAssistantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startExperimentActivity(course_teacher_assistant_list,position,"teacher_assistant");
            }
        });

        ListView CourseStudentListView = (ListView)findViewById(R.id.course_student_list);
        CourseStudentListView.setAdapter(new MyStudentAdapter());
        CourseStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startExperimentActivity(course_student_list,position,"student");
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            Intent intent = new Intent(CourseActivity.this,FavoriteActivity.class);
            Bundle data = new Bundle();
            String key_to_favorite = getString(R.string.key_to_favorite);
            data.putParcelable(key_to_favorite,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_experiment_history) {
            Intent intent = new Intent(CourseActivity.this,ExperimentHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_experiment_history = getString(R.string.key_to_experiment_history);
            data.putParcelable(key_to_experiment_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_upload_history) {
            Intent intent = new Intent(CourseActivity.this,UploadHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_upload_history = getString(R.string.key_to_upload_history);
            data.putParcelable(key_to_upload_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(CourseActivity.this,EditProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(CourseActivity.this,HelpActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_help);
            data.putParcelable(key_to_help,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(CourseActivity.this,SettingsActivity.class);
            Bundle data = new Bundle();
            String key_to_settings = getString(R.string.key_to_settings);
            data.putParcelable(key_to_settings,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCourses()
    {
//        PapaDataBaseManager papaDataBaseManager = PapaDataBaseManager.getInstance();
//
//        List<String> studentList = new ArrayList<String>();
//        List<String> assistList = new ArrayList<String>();
//
//        try {
//            papaDataBaseManager.getCourses(studentList, assistList);
//            course_student_list = studentList.toArray(new String[studentList.size()]);
//            course_teacher_assistant_list = assistList.toArray(new String[assistList.size()]);
//        }
//        catch (Exception e)
//        {
//            // TODO: What if courses cannot be received?
//
//        }
//        Please do not delete the code below when debugging:
        course_student_list = new String[3];
        for(int i = 0;i < 3;i ++){
            course_student_list[i] = "课程" + i;
        }
        course_teacher_assistant_list = new String[3];
        for(int i = 0;i < 3;i ++){
            course_teacher_assistant_list[i] = "课程" + i;
        }
    }

    private class MyTeacherAssistantAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return course_teacher_assistant_list.length;
        }
        @Override
        public Object getItem(int arg0){
            return arg0;
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(course_teacher_assistant_list[position]);
            mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
            mTextView.setTextColor(Color.parseColor(getString(R.string.color_primary)));
            return mTextView;
        }
    }

    private class MyStudentAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return course_student_list.length;
        }
        @Override
        public Object getItem(int arg0){
            return arg0;
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView mTextView = new TextView(getApplicationContext());
            mTextView.setText(course_student_list[position]);
            mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
            mTextView.setTextColor(Color.parseColor(getString(R.string.color_primary)));
            return mTextView;
        }
    }

    private void startExperimentActivity(String[] course_list,int position,String identity){
        Intent intent = new Intent(CourseActivity.this, ExperimentActivity.class);
        Bundle data = new Bundle();
        String key_course_experiment = getString(R.string.key_course_experiment);
        bundleHelper.setCourseName(course_list[position]);
        bundleHelper.setIdentity(identity);
        data.putParcelable(key_course_experiment,bundleHelper);
        intent.putExtras(data);
        startActivity(intent);
    }

}
