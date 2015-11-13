package com.Activities.papa.attendance;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerReal;
import com.Settings.Settings;

import java.util.Calendar;

public class Attendance {
    static final String TAG = "Attendance";
    static final int InterLessonPeriod = 1073741824;

    // don't need to serialize.
    transient PapaDataBaseManager papaDataBaseManager;
    private Attendance() {
        papaDataBaseManager = new PapaDataBaseManagerReal();
    }
    private static Attendance theInstance;
    public static Attendance getInstance() {
        if (theInstance == null)
            theInstance = new Attendance();
        return theInstance;
    }

    /**
     * Happily use this function in other activities to try sign in.
     * @param activity current activity
     */
    public static void startSignInByGPS(Activity activity) {
        // TODO: shouldn't clear cache every time
        Settings.clearCache(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Intent intent = new Intent(activity, LocationService.class);
        intent.putExtra(activity.getString(R.string.key_attendance_activity_command),
                activity.getString(R.string.key_attendance_activity_start_sign_in));
        activity.startService(intent);
    }

    /**
     * Try to sign in.
     * If we can sign in, notify the user and
     *  send sign in request and set a flag so that we won't sign in too many times.
     */
    public synchronized void trySignIn(OnSignInSuccessListener listener, Context context, String lessonId) {
        Settings settings = Settings.begin(context);
        if (canSignIn(settings)) {
            // send sign in request
            Log.w(TAG, "can sign in");

            // sign in and set signed in flags
            signIn(listener, settings, context, lessonId);
        }
        else if (haveSignedIn(settings)) {
            // already signed in
            // do nothing
            Log.w(TAG, "already signed in");
        }
        else {
            // missed a lesson
            Log.w(TAG, "missed a lesson");
            setNextLessonTime(settings, context);
        }
    }

    // TODO: should use lesson time
    private void setNextLessonTime(Settings s, Context context) {
        Calendar c = (Calendar) s.getNextSignInStartTime().clone();
        c.add(Calendar.MILLISECOND, InterLessonPeriod);
        s.setNextSignInStartTime(c);

        c = (Calendar) s.getNextSignInEndTime().clone();
        c.add(Calendar.MILLISECOND, InterLessonPeriod);
        s.setNextSignInEndTime(c);

        s.commit(context);
    }

    /**
     * Determines whether we can sign in now, by the lesson time in settings.
     * @param settings, the settings that save the lesson time.
     * @return result
     */
    private boolean canSignIn(Settings settings) {
        long current = System.currentTimeMillis();
        return settings.getNextSignInStartTime().getTimeInMillis() < current &&
                current < settings.getNextSignInEndTime().getTimeInMillis();
    }

    /**
     * Determines whether we have sign in.
     * @param settings, the settings that save the lesson time.
     * @return result
     */
    private boolean haveSignedIn(Settings settings) {
        return System.currentTimeMillis() < settings.getNextSignInStartTime().getTimeInMillis();
    }

    /**
     * Send real sign in request
     */
    private void signIn(final OnSignInSuccessListener listener, final Settings settings, final Context context, final String lessonId) {
        new AsyncTask<Object, Exception, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    papaDataBaseManager.postAttendance(new PapaDataBaseManager.PostAttendance(
                            settings.getToken(),
                            settings.getUserId(),
                            lessonId, 0, 0, true));
                    // TODO: how to check whether the post is successful
                    if (true) {
                        // these should move into network success callback
                        listener.onSignInSuccess();
                        setNextLessonTime(settings, context);
                    }
                } catch (PapaHttpClientException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
