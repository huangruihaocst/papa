package com.Activities.papa.settings;

import android.content.Context;

import com.Activities.papa.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

/**
 * User settings for Activities and Services in com.Activities.papa
 */
public class Settings implements Serializable {
    // for serialization, don't put it in xml
    String serialID = "1";

    /**
     * Initialize a default valid setting.
     */
    private Settings() {
        this.nextSignInStartTime = Calendar.getInstance();
        nextSignInStartTime.add(Calendar.SECOND, 10);
        this.nextSignInEndTime = Calendar.getInstance();
        nextSignInEndTime.add(Calendar.SECOND, 50);
    }

    // this lock avoids different thread instantiate different objs.
    private final static Object staticLock = new Object();
    private static Settings theInstance;
    public static Settings getInstance(Context context) {
        synchronized (staticLock) {
            if (theInstance == null) {
                theInstance = load(context);
            }
            return theInstance;
        }
    }
    public static Settings reload(Context context) {
        synchronized (staticLock) {
            theInstance = load(context);
            return theInstance;
        }
    }

    // These fields are for position tracking.
    double lastPositionLongitude;
    double lastPositionLatitude;
    double lastTargetPositionLongitude;
    double lastTargetPositionLatitude;
    Calendar lastRecordTime;
    Calendar lastSaveTime;
    Calendar lastSignInStartTime;
    Calendar lastSignInEndTime;
    Calendar nextSignInStartTime;
    Calendar nextSignInEndTime;

    // TODO: these copy by references may cause racing conditions.
    public synchronized Calendar getNextSignInStartTime() {
        return nextSignInStartTime;
    }
    public synchronized void setNextSignInStartTime(Calendar c) {
        nextSignInStartTime = c;
    }
    public synchronized Calendar getNextSignInEndTime() {
        return nextSignInEndTime;
    }
    public synchronized void setNextSignInEndTime(Calendar c) {
        nextSignInEndTime = c;
    }

    /**
     * Save the settings to some persistent storage
     * @param context, android context
     */
    synchronized public void commit(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(
                    context.getString(R.string.key_settings_file_name), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // set commit time
            lastSaveTime = Calendar.getInstance();
            oos.writeObject(this);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the settings from a particular persistent storage
     * @param context, android context
     */
    private static Settings load(Context context) {
        Settings settings;
        try {
            File file = new File(
                    context.getFilesDir().getPath() + "/" +
                            context.getString(R.string.key_settings_file_name));
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            settings = (Settings) ois.readObject();

            ois.close();
            fis.close();
            return settings;
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
            return new Settings();
        }
    }

    /**
     * Clear the cached settings.
     * @param context android context
     * @return whether cached settings exist.
     */
    public static boolean clearCache(Context context) {
        File file = new File(
                context.getFilesDir().getPath() + "/" +
                        context.getString(R.string.key_settings_file_name));
        return file.delete();
    }
}
