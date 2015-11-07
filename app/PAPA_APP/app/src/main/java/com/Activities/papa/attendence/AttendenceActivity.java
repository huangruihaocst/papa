package com.Activities.papa.attendence;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Activities.papa.R;

import java.security.Permission;
import java.util.List;

public class AttendenceActivity extends AppCompatActivity {
    static final String TAG = "AttendenceActivity";
    static final double CenterLongtitude = 0;
    static final double CenterLatitude = 0;
    static final double MinDistance = 100000;

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
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else {
            startRequestLocation();
        }
    }

    // meters
    static double EarthRadius = 6371000;

    double distanceToCenter(Location l) {
        // R*arccos[sin(wA)sin(wB)+cos(wA)cos(wB)*cos(jA-jB)]
        double wA = CenterLatitude;
        double jA = CenterLongtitude;
        double wB = l.getLatitude();
        double jB = l.getLongitude();

        return EarthRadius * Math.acos(
                Math.sin(wA) * Math.sin(wB) +
                        Math.cos(wA) * Math.cos(wB) * Math.cos(jA - jB)
        );
    }

    void startRequestLocation() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.w(TAG, "location changed");

                double dis = distanceToCenter(location);
                if (dis <= MinDistance) {
                    Toast.makeText(AttendenceActivity.this, "In classroom", Toast.LENGTH_SHORT).show();
                }
                else {
                   Toast.makeText(AttendenceActivity.this, "Out of classroom", Toast.LENGTH_SHORT).show();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.w(TAG, "status changed");
            }

            public void onProviderEnabled(String provider) {
                Log.w(TAG, "provider enabled");
            }

            public void onProviderDisabled(String provider) {
                Log.w(TAG, "provider disabled");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRequestLocation();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
