package com.Settings;

import android.content.Context;

import com.Activities.papa.R;
import com.Activities.papa.receive_message.MessageList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

        this.messageList = new MessageList();

        this.lessons.add(new Lesson("1", 0, 0, "1", "中国通史"));
    }

    // this lock avoids different thread instantiate different objs.
    private final static Object staticLock = new Object();
    private static Settings theInstance;
    public static Settings begin(Context context) {
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
    double      lastPositionLongitude;
    double      lastPositionLatitude;
    double      lastTargetPositionLongitude;
    double      lastTargetPositionLatitude;
    Calendar    lastRecordTime;
    Calendar    lastSaveTime;
    Calendar    lastSignInStartTime;
    Calendar    lastSignInEndTime;
    Calendar    nextSignInStartTime;
    Calendar    nextSignInEndTime;

    String      token;
    String      userId;

    public static class Lesson implements Serializable {
        public Lesson(String lessonId, double latitude, double longitude, String courseId, String courseName) {
            this.lessonId = lessonId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.courseId = courseId;
            this.courseName = courseName;
        }
        public String lessonId = "0";
        public double latitude = 0 ;
        public double longitude = 0;
        public String lessonName = "";
        public String courseId = "0";
        public String courseName = "";
    }
    ArrayList<Lesson> lessons = new ArrayList<>();

    // TODO: these copy by references may cause racing conditions.
    public synchronized Calendar    getNextSignInStartTime() {
        return nextSignInStartTime;
    }
    public synchronized void        setNextSignInStartTime(Calendar c) {
        nextSignInStartTime = c;
    }
    public synchronized Calendar    getNextSignInEndTime() {
        return nextSignInEndTime;
    }
    public synchronized void        setNextSignInEndTime(Calendar c) {
        nextSignInEndTime = c;
    }

    MessageList messageList;
    public synchronized MessageList getMessageList() {
        return this.messageList;
    }
    public synchronized void        setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }

    // For attendance
    public synchronized String      getToken() {
        return token;
    }
    public synchronized void        setToken(String token) {
        this.token = token;
    }
    public synchronized String      getUserId() {
        return userId;
    }
    public synchronized void        setUserId(String userId) {
        this.userId = userId;
    }
    public synchronized void        clearLessons() {
        lessons = new ArrayList<>();
    }
    public synchronized void        addLesson(String lessonId, double latitude, double longitude,
                                              String courseId, String courseName) {
        lessons.add(new Lesson(lessonId, latitude, longitude, courseId, courseName));
    }
    public synchronized Lesson      getLessonByLocation(double latitude, double longitude, double radius) {
        // TODO check real location
        for (int i = 0; i < lessons.size(); ++i) {
            double dis = distanceToCenter(latitude, longitude, lessons.get(i).latitude, lessons.get(i).longitude);
            if (true) {
            //if (dis < radius) {
                return lessons.get(i);
            }
        }
        return null;
    }

    /**
     * Save the settings to some persistent storage
     * @param context, android context
     */
    public synchronized void        commit(Context context) {
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
    private static Settings         load(Context context) {
        Settings settings;
        try {
            File file = new File(
                    context.getFilesDir().getPath() + "/" +
                            context.getString(R.string.key_settings_file_name));
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // settings
            // message list
            settings = (Settings) ois.readObject();

            ois.close();
            fis.close();
            return settings;
        } catch (IOException|ClassNotFoundException e) {
            //e.printStackTrace();
            return new Settings();
        }
    }

    /**
     * Clear the cached settings.
     * @param context android context
     * @return whether cached settings exist.
     */
    public static boolean           clearCache(Context context) {
        File file = new File(context.getFilesDir().getPath() + "/" +
                context.getString(R.string.key_settings_file_name));
        boolean ret = file.delete();
        reload(context);
        return ret;
    }

    static double EarthRadius = 6371000;
    /**
     * Helper function, calculates the distance by latitude and longitude
     * @param latA src latitude
     * @param longA source longitude
     * @return distance
     */
    static double distanceToCenter(double latA, double longA, double latB, double longB) {
        // R*arccos[sin(wA)sin(wB)+cos(wA)cos(wB)*cos(jA-jB)]
        return EarthRadius * Math.acos(
                Math.sin(longA) * Math.sin(latB) +
                        Math.cos(longA) * Math.cos(latB) * Math.cos(latA - longB)
        );
    }
}
