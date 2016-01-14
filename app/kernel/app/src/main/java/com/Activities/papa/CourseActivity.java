package com.Activities.papa;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.attendance.Attendance;
import com.Activities.papa.send_message.SentListActivity;
import com.Activities.papa.receive_message.MessageActivity;
import com.Activities.papa.receive_message.MessagePullService;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Fragments.papa.course.CourseFragment;
import com.Helpers.BundleHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.Helpers.ImageHelper;

public class CourseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String tag = "CourseActivity";
    BundleHelper bundleHelper = new BundleHelper();

    PapaDataBaseManager papaDataBaseManager;

    TabLayout tabLayout;
    ViewPager viewPager;

    int id;
    String token;

    /**
     * Indicating whether the connection to MessagePullService is establish.
     **/
    boolean bound;
    /**
     * Service connection callbacks.
     */
    private ServiceConnection connection;

    RelativeLayout relativeLayout;

    List<PapaDataBaseManager.TeacherInfo> teachersInfo;

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
        toolbar.setTitle(getString(R.string.main_title));
        setSupportActionBar(toolbar);

        this.papaDataBaseManager = bundleHelper.getPapaDataBaseManager();

        teachersInfo = null;

        tabLayout = (TabLayout) findViewById(R.id.semester_tab);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager)findViewById(R.id.course_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.requestDisallowInterceptTouchEvent(true);
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

        // Edit by Alex Wang 11-17, change the order to make sure tabLayout and
        //  viewPager are initialized before used in getSemester()
        getSemester();
        getTeachers();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        relativeLayout = (RelativeLayout)navigationView.inflateHeaderView(R.layout.nav_header_course);

        // Added by Alex Wang 2015-11-13. Move attendance service to course activity.
        Attendance.startSignInByGPS(this, token);
    }


    /**
     * Bind the service.
     */
    @Override
    public void onStart() {
        super.onStart();
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                bound = true;
                MessagePullService.LocalBinder binder = (MessagePullService.LocalBinder) service;
                final MessagePullService messagePullService = binder.getService();
                messagePullService.setUserInfo(String.valueOf(id), token);

                // start the polling procedure
                new AsyncTask<Object, Exception, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        messagePullService.startListen();
                        messagePullService.notifyMessagesNearingDeadline();
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };

        bindService(new Intent(this, MessagePullService.class),
                connection,
                Context.BIND_AUTO_CREATE);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        getHeaderView(navigationView);
    }

    /**
     * Unbind.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (bound)
            unbindService(connection);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(CourseActivity.this, ProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(CourseActivity.this, AboutActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_about);
            data.putParcelable(key_to_help, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(CourseActivity.this, MessageActivity.class);
            Bundle data = new Bundle();
            String key_to_notification = getString(R.string.key_to_notification);
            data.putParcelable(key_to_notification, bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if(id == R.id.nav_send_message){
            Intent intent = new Intent(CourseActivity.this, SentListActivity.class);
            Bundle data = new Bundle();
            String key_to_send_message = getString(R.string.key_to_sent_list);
            data.putParcelable(key_to_send_message, bundleHelper);
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

    void setSemester(List<Map.Entry<Integer, String>> h) {
        for(Iterator i = h.iterator(); i.hasNext();) {
            tabLayout.addTab(tabLayout.newTab().setText(
                    ((Map.Entry<Integer, String>) (i.next())).getValue())
            );
        }
        viewPager.setAdapter(
                new CourseViewPagerAdapter(
                        getSupportFragmentManager(), h.size(), h, id, token)
        );
    }



    //a function to change the profile in the navigation drawer, just call it in another thread
    private void getHeaderView(NavigationView navigationView){
        Log.i(tag, id + " " + token + " = id, token ");
        new GetUsrInfoTask(this).execute(new PapaDataBaseManager.UsrInfoRequest(id, token, getFilesDir()));
    }

    private void setHeaderView(PapaDataBaseManager.UsrInfoReply r){
        TextView username_label = (TextView) relativeLayout.findViewById(R.id.username_label);
        TextView mail_label = (TextView)findViewById(R.id.mail_label);
        ImageView image_label = (ImageView)findViewById(R.id.image_label);

        username_label.setText(r.usrInfo.usrName);
        mail_label.setText(r.usrInfo.mail);
        if(r.usrInfo.avatar.exists()) {
            Bitmap avatar = BitmapFactory.decodeFile(r.usrInfo.avatar.getAbsolutePath());
            //get window size
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            //resize
            Bitmap roundBitmap = ImageHelper.getRoundedCornerBitmap(avatar, avatar.getRowBytes() / 2);
            image_label.setImageBitmap(Bitmap.createScaledBitmap(roundBitmap,
                    (int)(width * 0.3), (int)(width * 0.3), false));
        }
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
                e.printStackTrace();
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
        int id;
        String token;
        List<Map.Entry<Integer, String>> lst;
        List<Fragment> fragment_cache;

        public CourseViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public CourseViewPagerAdapter(
                FragmentManager fm, int count, List<Map.Entry<Integer, String>> lst,
                int id, String token
        ) {
            this(fm);
            this.tabs_amount = count;
            this.id = id;
            this.token = token;
            this.lst = lst;
            this.fragment_cache = new ArrayList<>();
            for (int i = 0; i < lst.size(); ++i) {
                fragment_cache.add(null);
            }
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("CourseActivity", "getItem");
            Fragment cachedFragment = fragment_cache.get(position);
            if (cachedFragment == null) {
                cachedFragment = CourseFragment.newInstance(id, lst.get(position).getKey(), token, bundleHelper);
                fragment_cache.set(position, cachedFragment);
            }
            return cachedFragment;
        }

        @Override
        public int getCount() {
            return tabs_amount;
        }

    }


    class GetTeachersInfoTask extends
            AsyncTask<PapaDataBaseManager.GetTeachersInfoRequest,
                    Exception, PapaDataBaseManager.GetTeachersInfoReply> {
        ProgressDialog proDialog;

        public GetTeachersInfoTask(Context context) {
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
        protected PapaDataBaseManager.GetTeachersInfoReply doInBackground
                (PapaDataBaseManager.GetTeachersInfoRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getTeachersInfo(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.GetTeachersInfoReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setTeachers(rlt.list);
        }
    }

    void getTeachers() {
        new GetTeachersInfoTask(this).execute(new PapaDataBaseManager.GetTeachersInfoRequest(
                token, String.valueOf(id)
        ));
    }

    void setTeachers(List<PapaDataBaseManager.TeacherInfo> lst) {
        teachersInfo = lst;
        bundleHelper.setTeachersInfo(teachersInfo);
    }
}
