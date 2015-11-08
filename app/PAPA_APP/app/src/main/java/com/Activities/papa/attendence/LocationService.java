package com.Activities.papa.attendence;

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
import com.Activities.papa.settings.Settings;

import java.util.Calendar;
import java.util.TimeZone;


public class LocationService extends Service {
    static final String TAG = "LocationService";
    static double EarthRadius = 6371000;
    static final double CenterLongitude = 0;
    static final double CenterLatitude = 0;
    static final double MinDistance = 10000000;
    static final int RetryInterval = 1000;
    // it means the lesson starts only once.
    static final int InterLessonPeriod = 1073741824;

    Settings settings;

    public LocationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra(getString(R.string.key_attendence_activity_command));
        if (command != null && command.equals(getString(R.string.key_attendence_activity_start_sign_in))) {
            // TODO: shouldn't clear cache every time
            Settings.clearCache(this);
            settings = Settings.getInstance(this);
            startTrackingPosition();
        }

        return START_NOT_STICKY;
    }

    public boolean startTrackingPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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

            // TODO 1
            // get center location and min distance from server

            // calculate location from center
            double dis = distanceToCenter(CenterLatitude, CenterLongitude, location);

            // check distance,
            if (dis <= MinDistance) {
                // sign in
                Log.w(TAG, "In classroom, distance: " + String.valueOf(dis));
                Attendence.getInstance().trySignIn(new OnSignInSuccessListener() {
                    @Override
                    public void onSignInSuccess() {
                        stopTrackingPosition();
                        notifySignInSuccessful("Operating System", Calendar.getInstance(), "Alex", "GPS");
                    }
                }, LocationService.this);
            }
            else {
                Log.w(TAG, "Out of classroom distance: " + String.valueOf(dis));
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
                .setContentTitle(String.format(getString(R.string.title_attendence_activity_sign_in_successful), name, lesson))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentText("Time: " + time.getTime().toString() + " method: " + method)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(getResources().getInteger(R.integer.key_message_pull_notification_id), notification);
    }

    /**
     * Helper function, calculates the distance by latitude and longitude
     * @param latA src latitude
     * @param longA source longitude
     * @param l current location
     * @return distance
     */
    static double distanceToCenter(double latA, double longA, Location l) {
        // R*arccos[sin(wA)sin(wB)+cos(wA)cos(wB)*cos(jA-jB)]
        double latB = l.getLatitude();
        double longB = l.getLongitude();

        return EarthRadius * Math.acos(
                Math.sin(longA) * Math.sin(latB) +
                        Math.cos(longA) * Math.cos(latB) * Math.cos(latA - longB)
        );
    }

    /**
     * This function is just to make the compiler happy
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Won't be implemented");
    }
}
