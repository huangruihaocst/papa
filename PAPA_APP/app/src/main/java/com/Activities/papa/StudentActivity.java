package com.Activities.papa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] student_list;
    BundleHelper bundleHelper = new BundleHelper();

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

        getStudents(experiment_name);

        final ListView StudentListView = (ListView)findViewById(R.id.student_list);
        StudentListView.setAdapter(new MyAdapter());
        StudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentActivity.this,DetailActivity.class);
                String key_to_detail = getString(R.string.key_to_detail);
                Bundle data = new Bundle();
                data.putParcelable(key_to_detail,bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
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

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(StudentActivity.this,HelpActivity.class);
            Bundle data = new Bundle();
            String key_course_help = getString(R.string.key_course_help);
            data.putParcelable(key_course_help,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount(){
            return student_list.length;
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
            mTextView.setText(student_list[position]);
            mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
            mTextView.setTextColor(Color.parseColor(getString(R.string.color_primary)));
            return mTextView;
        }
    }

    private void getStudents(String experiment_name){
        student_list = new String[10];
        for(int i = 0;i < 10;i ++){
            student_list[i] = "学生" + i;
        }
    }
}
