package com.Activities.papa;

import android.os.Parcel;
import android.os.Parcelable;

import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerJiaDe;

/**
 * Created by huang on 15-10-10.
 */
public class BundleHelper implements Parcelable{
    private String username;
    private String password;//not used
    private String course_name;
    private int course_id;
    private String experiment_name;
    private int experiment_id;
    private String student_name;
    private int student_id;
    private String identity;


    private int id;
    private String token;
//    private JSONObject jsonObject;
//    private String getter_string;

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
        id = -1;
        token = "";
//        jsonObject = new JSONObject();
//        getter_string = jsonObject.toString();
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
    public int getId(){
        return id;
    }
    public String getToken(){
        return token;
    }
//    public PapaTelephoneNumberGetter getPapaTelephoneNumberGetter(){
//        try{
//            jsonObject = new JSONObject(getter_string);
//        }catch (JSONException e){
//            Log.e("parse", e.getMessage());
//        }
//        PapaTelephoneNumberGetter papaTelephoneNumberGetter = null;
//        try{
//            Object object = jsonObject.get("getter");
//            papaTelephoneNumberGetter = (PapaTelephoneNumberGetter)object;
//        }catch (JSONException e){
//            Log.e("parse", e.getMessage());
//        }
//        return papaTelephoneNumberGetter;
//    }


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
    public void setId(int id){
        this.id = id;
    }
    public void setToken(String token){
        this.token = token;
    }
//    public void setPapaTelephoneNumberGetter(PapaTelephoneNumberGetter papaTelephoneNumberGetter){
//        try{
//            jsonObject.putOpt("getter",papaTelephoneNumberGetter);
//        }catch (JSONException e){
//            Log.e("serialize", e.getMessage());
//        }
//        getter_string = jsonObject.toString();
//    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(password);
        out.writeString(course_name);
        out.writeInt(course_id);
        out.writeString(experiment_name);
        out.writeInt(experiment_id);
        out.writeString(student_name);
        out.writeInt(student_id);
        out.writeString(identity);
        out.writeInt(id);
        out.writeString(token);
//        out.writeString(getter_string);
    }

    public static final Parcelable.Creator<BundleHelper> CREATOR = new Parcelable.Creator<BundleHelper>() {
        public BundleHelper createFromParcel(Parcel in) {
            return new BundleHelper(in);
        }

        public BundleHelper[] newArray(int size) {
            return new BundleHelper[size];
        }
    };

    private BundleHelper(Parcel in){
        username = in.readString();
        password = in.readString();
        course_name = in.readString();
        course_id = in.readInt();
        experiment_name = in.readString();
        experiment_id = in.readInt();
        student_name = in.readString();
        student_id = in.readInt();
        identity = in.readString();
        id = in.readInt();
        token = in.readString();
//        getter_string = in.readString();
    }

    public PapaDataBaseManager getPapaDataBaseManager()
    {
        return new PapaDataBaseManagerJiaDe();
    }
}

