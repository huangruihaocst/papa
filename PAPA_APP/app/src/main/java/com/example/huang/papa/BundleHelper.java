package com.example.huang.papa;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huang on 15-10-10.
 */
public class BundleHelper {
    private String username;
    private String password;
    private String course_name;
    private int course_id;
    private String experiment_name;
    private int experiment_id;
    private String student_name;
    private int student_id;
    private String identity;


    public BundleHelper(){
        username = "";
        password = "";
        course_name = "";
        course_id = -1;
        experiment_name = "";
        experiment_id = -1;
        student_name = "";
        student_id = -1;
        identity = "";
    }


    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getCourseName(){
        return course_name;
    }
    public int getCourseId(){
        return  course_id;
    }
    public String getExperimentName(){
        return experiment_name;
    }
    public int getExperiment_id(){
        return experiment_id;
    }
    public String getStudentName(){
        return student_name;
    }
    public int getStudentId(){
        return student_id;
    }
    public String getIdentity(){
        return identity;
    }


    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setCourseName(String course_name){
        this.course_name = course_name;
    }
    public void setCourseId(int course_id){
        this.course_id = course_id;
    }
    public void setExperimentName(String experiment_name){
        this.experiment_name = experiment_name;
    }
    public void setExperiment_id(int experiment_id){
        this.experiment_id = experiment_id;
    }
    public void setStudentName(String student_name){
        this.student_name = student_name;
    }
    public void getStudentId(int student_id){
        this.student_id = student_id;
    }
    public void setIdentity(String identity){
        this.identity = identity;
    }
}

//public class MyParcelable implements Parcelable{
//    private int mData;
//    private int jb;
//
//    /* everything below here is for implementing Parcelable */
//
//    // 99.9% of the time you can just ignore this
//    public int describeContents() {
//        return 0;
//    }
//
//    // write your object's data to the passed-in Parcel
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeInt(mData);
//        out.writeInt(jb);
//    }
//
//    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
//    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {
//        public MyParcelable createFromParcel(Parcel in) {
//            return new MyParcelable(in);
//        }
//
//        public MyParcelable[] newArray(int size) {
//            return new MyParcelable[size];
//        }
//    };
//
//    // example constructor that takes a Parcel and gives you an object populated with it's values
//    private MyParcelable(Parcel in) {
//        mData = in.readInt();
//        jb = in.readInt();
//    }
//}

