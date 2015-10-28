package com.Activities.papa;

import android.content.Intent;
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

public class ExperimentDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

//        if (id == R.id.nav_favorite) {
//            Intent intent = new Intent(ExperimentActivity.this,FavoriteActivity.class);
//            Bundle data = new Bundle();
//            String key_to_favorite = getString(R.string.key_to_favorite);
//            data.putParcelable(key_to_favorite,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if (id == R.id.nav_experiment_history) {
//            Intent intent = new Intent(ExperimentActivity.this,ExperimentHistoryActivity.class);
//            Bundle data = new Bundle();
//            String key_to_experiment_history = getString(R.string.key_to_experiment_history);
//            data.putParcelable(key_to_experiment_history,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if (id == R.id.nav_upload_history) {
//            Intent intent = new Intent(ExperimentActivity.this,UploadHistoryActivity.class);
//            Bundle data = new Bundle();
//            String key_to_upload_history = getString(R.string.key_to_upload_history);
//            data.putParcelable(key_to_upload_history,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if (id == R.id.nav_edit_profile) {
//            Intent intent = new Intent(ExperimentActivity.this,EditProfileActivity.class);
//            Bundle data = new Bundle();
//            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
//            data.putParcelable(key_to_edit_profile,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if (id == R.id.nav_help) {
//            Intent intent = new Intent(ExperimentActivity.this,HelpActivity.class);
//            Bundle data = new Bundle();
//            String key_to_help = getString(R.string.key_to_help);
//            data.putParcelable(key_to_help,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if (id == R.id.nav_settings) {
//            Intent intent = new Intent(ExperimentActivity.this,SettingsActivity.class);
//            Bundle data = new Bundle();
//            String key_to_settings = getString(R.string.key_to_settings);
//            data.putParcelable(key_to_settings,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        } else if(id == R.id.nav_notification){
//            Intent intent = new Intent(ExperimentActivity.this,NotificationActivity.class);
//            Bundle data = new Bundle();
//            String key_to_notification = getString(R.string.key_to_notification);
//            data.putParcelable(key_to_notification,bundleHelper);
//            intent.putExtras(data);
//            startActivity(intent);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
