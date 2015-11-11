package com.Activities.papa.attendance;

import android.content.Context;
import android.util.Log;

import com.Settings.Settings;

import java.util.Calendar;

public class Attendance {
    static final String TAG = "Attendance";
    static final int InterLessonPeriod = 1073741824;

    private static Attendance theInstance;
    public static Attendance getInstance() {
        if (theInstance == null)
            theInstance = new Attendance();
        return theInstance;
    }

    /**
     * Try to sign in.
     * If we can sign in, notify the user and
     *  send sign in request and set a flag so that we won't sign in too many times.
     */
    public synchronized void trySignIn(OnSignInSuccessListener listener, Context context) {
        Settings settings = Settings.getInstance(context);
        if (canSignIn(settings)) {
            // send sign in request
            Log.w(TAG, "can sign in");

            // sign in and set signed in flags
            signIn(listener, settings, context);
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
    private void signIn(OnSignInSuccessListener listener, Settings settings, Context context) {
        // TODO 2 access the network

        // these should move into network success callback
        listener.onSignInSuccess();
        setNextLessonTime(settings, context);
    }
}
