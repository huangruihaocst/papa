package com.Activities.papa;

import android.os.Parcel;
import android.os.Parcelable;

import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerReal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 15-10-10.
 */
public class BundleHelper implements Parcelable{
    public enum Identity{
        teacher_assistant,
        student,
        no_identity
    }
    private String username;
    private String course_name;
    private int course_id;
    private String experiment_name;
    private int experiment_id;
    private String student_name;
    private int student_id;
    private Identity identity;
    private int id;
    private String token;
    private String sender_name;
    private ArrayList<PapaDataBaseManager.TeacherInfo> teachers_info;
    private PapaDataBaseManager.ChatMessage chat_message;
//    private JSONObject jsonObject;
//    private String getter_string;

    public BundleHelper(){
        username = "";
        course_name = "";
        course_id = -1;
        experiment_name = "";
        experiment_id = -1;
        student_name = "";
        student_id = -1;
        identity = Identity.no_identity;
        id = -1;
        token = "";
        sender_name = "";
//        jsonObject = new JSONObject();
//        getter_string = jsonObject.toString();
        teachers_info = new ArrayList<>();
        chat_message = new PapaDataBaseManager.ChatMessage("","","","","","");
    }


    public String getUsername(){
        return username;
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
    public int getExperimentId(){
        return experiment_id;
    }
    public String getStudentName(){
        return student_name;
    }
    public int getStudentId(){
        return student_id;
    }
    public Identity getIdentity(){
        return identity;
    }
    public int getId(){
        return id;
    }
    public String getToken(){
        return token;
    }
    public String getSenderName(){
        return sender_name;
    }
    public ArrayList<PapaDataBaseManager.TeacherInfo> getTeachersInfo(){
        return teachers_info;
    }
    public PapaDataBaseManager.ChatMessage getChatMessage(){
        return chat_message;
    }

    public void setUsername(String username){
        this.username = username;
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
    public void setStudentId(int student_id){
        this.student_id = student_id;
    }
    public void setIdentity(Identity identity){
        this.identity = identity;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setToken(String token){
        this.token = token;
    }
    public void setSenderName(String sender_name){
        this.sender_name = sender_name;
    }
    public void setTeachersInfo(List<PapaDataBaseManager.TeacherInfo> teachers_info){
        this.teachers_info = (ArrayList<PapaDataBaseManager.TeacherInfo>)teachers_info;
    }
    public void setChatMessage(PapaDataBaseManager.ChatMessage chat_message){
        this.chat_message = chat_message;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(course_name);
        out.writeInt(course_id);
        out.writeString(experiment_name);
        out.writeInt(experiment_id);
        out.writeString(student_name);
        out.writeInt(student_id);
        out.writeString(identity.toString());
        out.writeInt(id);
        out.writeString(token);
        out.writeString(sender_name);
        out.writeTypedList(teachers_info);
        out.writeParcelable(chat_message,flags);
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
        course_name = in.readString();
        course_id = in.readInt();
        experiment_name = in.readString();
        experiment_id = in.readInt();
        student_name = in.readString();
        student_id = in.readInt();
        String identity_string = in.readString();
        switch (identity_string){
            case "teacher_assistant":
                identity = Identity.teacher_assistant;
                break;
            case "student":
                identity = Identity.student;
                break;
            case "no_identity":
                identity = Identity.no_identity;
                break;
        }
        id = in.readInt();
        token = in.readString();
        sender_name = in.readString();
        teachers_info = new ArrayList<>();
        in.readTypedList(teachers_info, PapaDataBaseManager.TeacherInfo.CREATOR);
        chat_message = in.readParcelable(PapaDataBaseManager.ChatMessage.class.getClassLoader());
    }

    static public PapaDataBaseManager getPapaDataBaseManager()
    {
        // return new PapaDataBaseManagerJiaDe();

        return new PapaDataBaseManagerReal();
    }

}

