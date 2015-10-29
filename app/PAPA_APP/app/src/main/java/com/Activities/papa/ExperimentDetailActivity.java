package com.Activities.papa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.Fragments.papa.ExperimentInformationFragment;
import com.Fragments.papa.ExperimentResultFragment;
import com.Fragments.papa.GradesFragment;
import com.Fragments.papa.StudentsFragment;

public class ExperimentDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BundleHelper bundleHelper;
    String identity;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_experiment_experiment_detail = getString(R.string.key_experiment_experiment_detail);
        bundleHelper = data.getParcelable(key_experiment_experiment_detail);

        setContentView(R.layout.activity_experiment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(bundleHelper.getExperimentName());
        setSupportActionBar(toolbar);

        identity = bundleHelper.getIdentity();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        tabLayout = (TabLayout)findViewById(R.id.option_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.experiment_information)));
        if(identity.equals("teacher_assistant"))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.view_students)));
        else if(identity.equals("student"))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.view_grades)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.experiment_result)));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.options_view_pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new OptionsAdapter(getSupportFragmentManager(),3,identity));
        viewPager.setOffscreenPageLimit(3);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button button_upload = (Button)findViewById(R.id.button_upload);
        if(identity.equals("teacher_assistant")){
            button_upload.setVisibility(View.GONE);
        }
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExperimentDetailActivity.this);
                builder.setTitle(getString(R.string.select_type)).setItems(R.array.upload_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){//camera

                        }else if(which == 1){//video

                        }else if(which == 2){//gallery
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Button button_custom = (Button)findViewById(R.id.button_custom);
        if(identity.equals("student")){
            button_custom.setText(getString(R.string.view_grades));
        }

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
        getMenuInflater().inflate(R.menu.experiment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            Intent intent = new Intent(ExperimentDetailActivity.this,FavoriteActivity.class);
            Bundle data = new Bundle();
            String key_to_favorite = getString(R.string.key_to_favorite);
            data.putParcelable(key_to_favorite,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_experiment_history) {
            Intent intent = new Intent(ExperimentDetailActivity.this,ExperimentHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_ExperimentDetail_history = getString(R.string.key_to_experiment_history);
            data.putParcelable(key_to_ExperimentDetail_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_upload_history) {
            Intent intent = new Intent(ExperimentDetailActivity.this,UploadHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_upload_history = getString(R.string.key_to_upload_history);
            data.putParcelable(key_to_upload_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(ExperimentDetailActivity.this,EditProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(ExperimentDetailActivity.this,HelpActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_help);
            data.putParcelable(key_to_help,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ExperimentDetailActivity.this,SettingsActivity.class);
            Bundle data = new Bundle();
            String key_to_settings = getString(R.string.key_to_settings);
            data.putParcelable(key_to_settings,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if(id == R.id.nav_notification){
            Intent intent = new Intent(ExperimentDetailActivity.this,NotificationActivity.class);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
//        Toast.makeText(getApplicationContext(),selectedImagePath,Toast.LENGTH_LONG).show();
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO: perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public class OptionsAdapter extends FragmentStatePagerAdapter{
        int tabs_amount;
        String identity;

        public OptionsAdapter(FragmentManager fm) {
            super(fm);
        }
        public OptionsAdapter(FragmentManager fm, int count, String identity) {
            this(fm);
            this.tabs_amount = count;
            this.identity = identity;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            switch (position){
                case 0:
                    fragment = ExperimentInformationFragment.newInstance("1","2");
                    break;
                case 1:
                    if (identity.equals("teacher_assistant")){
                        fragment = StudentsFragment.newInstance("1","2");
                    }else if(identity.equals("student")){
                        fragment = GradesFragment.newInstance("1","2");
                    }
                    break;
                case 2:
                    fragment = ExperimentResultFragment.newInstance("1","2");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabs_amount;
        }

    }
}
