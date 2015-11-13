package com.Activities.papa.attendance;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.Activities.papa.R;
import com.Settings.Settings;

import java.util.Calendar;
import java.util.TimeZone;


public class LocationService extends Service {
    static final String TAG = "LocationService";

    static final double MinDistance = 10000000;
    static final int RetryInterval = 1000;

    Settings settings;

    public LocationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra(getString(R.string.key_attendance_activity_command));
        if (command != null && command.equals(getString(R.string.key_attendance_activity_start_sign_in))) {
            settings = Settings.begin(this);
            startTrackingPosition();
        }

        return START_NOT_STICKY;
    }

    public boolean startTrackingPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    void stopTrackingPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.w(TAG, "location changed");
            Log.w(TAG, String.format("Location: %f, %f", location.getLatitude(), location.getLongitude()));
            Log.w(TAG, String.format("Location accuracy: %f", location.getAccuracy()));

            // TODO 1
            // get center location and min distance from server
            final String courseName = "Operating System";
            final String userName = "Alex";

            // calculate location from center
            String lessonId = settings.getLessonByLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    MinDistance + location.getAccuracy());

            // check distance + accuracy
            if (lessonId != null) {
                // sign in
                Attendance.getInstance().trySignIn(new OnSignInSuccessListener() {
                    @Override
                    public void onSignInSuccess() {
                        stopTrackingPosition();
                        notifySignInSuccessful(courseName, Calendar.getInstance(), userName, "GPS");
                    }
                }, LocationService.this, lessonId);
            }
            else {
                Log.w(TAG, "Out of classroom");
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

    /**
     * Notify the user that he has successfully signed in.
     * @param lesson the lesson he has signed in
     * @param time the time of the sign in request
     * @param name user name
     * @param method sign in method
     */
    void notifySignInSuccessful(String lesson, Calendar time, String name, String method) {
        time.setTimeZone(TimeZone.getDefault());
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(String.format(getString(R.string.title_attendance_activity_sign_in_successful), name, lesson))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentText(String.format(getString(
                                        R.string.title_attendance_activity_sign_in_content),
                                        Calendar.getInstance().toString(),
                        method))
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(getResources().getInteger(R.integer.key_message_pull_notification_id), notification);
    }

    /**
     * This function is just to make the compiler happy
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Won't be implemented");
    }
}
