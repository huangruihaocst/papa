package com.Activities.papa.attendence;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.Activities.papa.R;
import com.Activities.papa.settings.Settings;

public class AttendenceActivity extends AppCompatActivity {
    static final String TAG = "AttendenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Locating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            startSignInByGPS(AttendenceActivity.this);
            }
        });


        // TODO: shouldn't clear cache every time
        Settings.clearCache(this);
    }

    /**
     * Happily use this function to try sign in.
     * @param activity current activity
     */
    static void startSignInByGPS(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Intent intent = new Intent(activity, LocationService.class);
        intent.putExtra(activity.getString(R.string.key_attendence_activity_command),
                activity.getString(R.string.key_attendence_activity_start_sign_in));
        activity.startService(intent);
    }

    static void startSignInByWifi(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Intent intent = new Intent(activity, LocationService.class);
        intent.putExtra(activity.getString(R.string.key_attendence_activity_command),
                activity.getString(R.string.key_attendence_activity_start_sign_in));
        activity.startService(intent);
    }
}
