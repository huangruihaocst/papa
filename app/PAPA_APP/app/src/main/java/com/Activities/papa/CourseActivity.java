package com.Activities.papa;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CourseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String tag = "CourseActivity";
    BundleHelper bundleHelper = new BundleHelper();

    PapaDataBaseManager papaDataBaseManager;

    TabLayout tabLayout;
    ViewPager viewPager;

    int id;
    String token;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_sign_in_course = getString(R.string.key_sign_in_course);
        bundleHelper = data.getParcelable(key_sign_in_course);

        id = bundleHelper.getId();
        token = bundleHelper.getToken();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.hint_select_course));
        setSupportActionBar(toolbar);

        this.papaDataBaseManager = bundleHelper.getPapaDataBaseManager();

        getSemester();

        tabLayout = (TabLayout) findViewById(R.id.semester_tab);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager = (ViewPager)findViewById(R.id.course_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (bundleHelper.getIdentity().equals("teacher_assistant")) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_upload_history);
            item.setVisible(false);
        }
        linearLayout = (LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_course);

        getHeaderView(navigationView);

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
            Intent intent = new Intent(CourseActivity.this, FavoriteActivity.class);
            Bundle data = new Bundle();
            String key_to_favorite = getString(R.string.key_to_favorite);
            data.putParcelable(key_to_favorite, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_experiment_history) {
            Intent intent = new Intent(CourseActivity.this, ExperimentHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_experiment_history = getString(R.string.key_to_experiment_history);
            data.putParcelable(key_to_experiment_history, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_upload_history) {
            Intent intent = new Intent(CourseActivity.this, UploadHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_upload_history = getString(R.string.key_to_upload_history);
            data.putParcelable(key_to_upload_history, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(CourseActivity.this, EditProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(CourseActivity.this, HelpActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_help);
            data.putParcelable(key_to_help, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(CourseActivity.this, SettingsActivity.class);
            Bundle data = new Bundle();
            String key_to_settings = getString(R.string.key_to_settings);
            data.putParcelable(key_to_settings, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(CourseActivity.this, NotificationActivity.class);
            Bundle data = new Bundle();
            String key_to_notification = getString(R.string.key_to_notification);
            data.putParcelable(key_to_notification, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    // Semester

    private void getSemester() {
        GetSemesterTask task = new GetSemesterTask(this);
        task.execute();
    }

    class GetSemesterTask extends AsyncTask<Void, Exception, PapaDataBaseManager.SemesterReply> {
        ProgressDialog proDialog;

        public GetSemesterTask(Context context) {
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
        protected PapaDataBaseManager.SemesterReply doInBackground
                (Void... params) {
            // 在后台
            try {
                return papaDataBaseManager.getSemester();
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
        protected void onPostExecute(PapaDataBaseManager.SemesterReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setSemester(rlt.semester);
        }
    }


    void setSemester(HashMap h) {
        Iterator iterator = h.keySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Object key = iterator.next();
            tabLayout.addTab(tabLayout.newTab().setText((String) h.get(key)));
            count++;
        }
        viewPager.setAdapter(new CourseViewPagerAdapter(getSupportFragmentManager(), count, id, token));
    }



    //a function to change the profile in the navigation drawer, just call it in another thread
    private void getHeaderView(NavigationView navigationView){
        Log.i(tag, id + " " + token + " = id, token ");
        new GetUsrInfoTask(this).execute(new PapaDataBaseManager.UsrInfoRequest(id, token));
    }

    private void setHeaderView(PapaDataBaseManager.UsrInfoReply r){
        TextView username_label = (TextView)linearLayout.findViewById(R.id.username_label);
        TextView mail_label = (TextView)findViewById(R.id.mail_label);
        ImageView image_label = (ImageView)findViewById(R.id.image_label);

        username_label.setText(r.usrName);
        mail_label.setText(r.mail);
    }


    class GetUsrInfoTask extends
            AsyncTask<PapaDataBaseManager.UsrInfoRequest, Exception, PapaDataBaseManager.UsrInfoReply> {
        ProgressDialog proDialog;

        public GetUsrInfoTask(Context context) {
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
        protected PapaDataBaseManager.UsrInfoReply doInBackground
                (PapaDataBaseManager.UsrInfoRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getUsrInfo(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.UsrInfoReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setHeaderView(rlt);
        }
    }

    public class CourseViewPagerAdapter extends FragmentStatePagerAdapter{
        int tabs_amount;
        int semester_id;
        String token;

        public CourseViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public CourseViewPagerAdapter(FragmentManager fm, int count, int id, String token) {
            this(fm);
            this.tabs_amount = count;
            semester_id = id;
            this.token = token;
        }

        @Override
        public Fragment getItem(int position) {
            return CourseFragment.newInstance(semester_id, token);
        }

        @Override
        public int getCount() {
            return tabs_amount;
        }

    }
}
