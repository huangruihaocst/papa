
/**
 * Created by shyo on 15-10-9.
 *
 * Papa DataBase Manager
 *
 */
package com.Back.PapaDataBaseManager.papa;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.Activities.papa.receive_message.Message;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Fragments.papa.experiment_result.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public abstract class PapaDataBaseManager{


    // 登录
    static public class SignInRequest
    {
        public String id;
        public String pwd;

        public SignInRequest(String id, String pwd)
        {
            this.id = id;
            this.pwd = pwd;
        }
    }

    static public class SignInReply
    {
        public int personId;
        public String token;

        public SignInReply(int personId, String token)
        {
            this.personId = personId;
            this.token = token;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学期
    static public class SemesterReply
    {
        public List<Map.Entry<Integer, String>> semester;

        public SemesterReply()
        {
            semester = new ArrayList<>();
        }
    }

    public abstract SemesterReply getSemester() throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程 Course
    static public class CourseRequest
    {
        public int id;
        public int semesterId;
        public String token;

        public CourseRequest(int id, int semesterId, String token)
        {
            this.id = id;
            this.semesterId = semesterId;
            this.token = token;
        }
    }

    static public class CourseReply
    {
        public List<Map.Entry<Integer, String>> course;

        public CourseReply()
        {
            course = new ArrayList<>();
        }
    }

    public abstract CourseReply getStuCourse(CourseRequest request) throws PapaHttpClientException;
    public abstract CourseReply getTACourse(CourseRequest request) throws PapaHttpClientException;



    //////////////////////////////////////////////////////////////////////////////////////////////

    // 获取学生课程 Lesson
    static public class LessonRequest
    {
        public int courseId;

        public LessonRequest(int courseId)
        {
            this.courseId = courseId;
        }
    }

    static public class LessonInfo
    {
        public String name;
        public Calendar startTime;
        public Calendar endTime;
        public String location;

        public String description;

        public LessonInfo(String name, String description, Calendar startTime, Calendar endTime, String location)
        {
            this.name = name;
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }
    }

    static public class LessonReply
    {
        public List<Map.Entry<Integer, LessonInfo>> lesson;

        public LessonReply()
        {
            lesson = new ArrayList<>();
        }
    }

    public abstract LessonReply getLesson(LessonRequest request) throws PapaHttpClientException;



    /////////////////////////////////////////////////////////////////////////////////////////////
    // 获取用户信息
    static public class UsrInfoRequest
    {
        public int id;
        public String token;
        public File file;

        public UsrInfoRequest(int id, String token, File file)
        {
            this.id = id;
            this.token = token;
            this.file = file;
        }
    }

    static public class UsrInfo
    {
        public String usrName;
        public String mail;
        public String phone;
        public File avatar;

        public UsrInfo(String usrName, String mail, String phone, File avatar)
        {
            this.usrName = usrName;
            this.mail = mail;
            this.phone = phone;
            this.avatar = avatar;
        }
    }

    static public class UsrInfoReply
    {
        public int id;
        public UsrInfo usrInfo;

        public UsrInfoReply(int id, UsrInfo usrInfo)
        {
            this.id = id;
            this.usrInfo = usrInfo;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract UsrInfoReply getUsrInfo(UsrInfoRequest request) throws PapaHttpClientException;


    /////////////////////////////////////////////////////////////////////////////////////////////
    // 获取用户信息
    static public class PutUsrInfoRequest
    {
        public int id;
        public String token;
        public UsrInfo usrInfo;

        public PutUsrInfoRequest(int id, String token, UsrInfo usrInfo)
        {
            this.id = id;
            this.token = token;
            this.usrInfo = usrInfo;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract void putUsrInfo(PutUsrInfoRequest request) throws PapaHttpClientException;

    /////////////////////////////////////////////////////////////////////////////////////////////
    // 换密码
    static public class PutUsrPasswordRequest
    {
        public String token;
        public String personId;
        public String password;

        public PutUsrPasswordRequest(String token, String personId, String password)
        {
            this.token = token;
            this.password = password;
        }
    }

    // 使用 POST 方法登录 返回是否成功
    public abstract void PutUsrPassword(PutUsrPasswordRequest request) throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////

    // 助教获取学生列表

    static public class StudentsRequest
    {
        public int courseId;
        public String token;

        public StudentsRequest(int courseId, String token)
        {
            this.courseId = courseId;
            this.token = token;
        }
    }

    static public class StudentInfo
    {
        public String studentId;
        public String name;
        public String studentNumber;
        public String phone;
        public String avatarId;
        public String email;
        public String description;
        public String department;
        public String className;

        StudentInfo(
                String studentId, String name, String studentNumber,
                String phone, String avatarId, String email,
                String description, String department, String className)
        {
            this.studentId = studentId;
            this.name = name;
            this.studentNumber = studentNumber;
            this.phone = phone;
            this.avatarId = avatarId;
            this.email = email;
            this.description = description;
            this.department = department;
            this.className = className;
        }
    }

    static public class StudentsReply
    {
        public List<Map.Entry<Integer, StudentInfo>> students;

        public StudentsReply()
        {
            students = new ArrayList<>();
        }
    }

    public abstract StudentsReply getStudents(StudentsRequest request)
            throws PapaHttpClientException;

    /*
        吐血推荐
        http://www.bilibili.com/video/av2098846/#page=2

        aLIEz + 霍元甲
        良心作
     */


    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取实验详细信息

    static public class GetLessonInfoRequest
    {
        public int lessonId;
        public String token;

        public GetLessonInfoRequest(int lessonId, String token)
        {
            this.lessonId = lessonId;
            this.token = token;
        }
    }

    static public class GetLessonInfoReply
    {
        public String name;
        public Calendar startTime;
        public Calendar endTime;
        public String location;
        public GetLessonInfoReply(String name, Calendar startTime, Calendar endTime, String location)
        {
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }
    }

    public abstract GetLessonInfoReply getLessonInfo(GetLessonInfoRequest request)
            throws PapaHttpClientException;

    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取助教对学生的评分

    static public class GetCommentsRequest
    {
        public int lessonId;
        public int personId;
        public String token;

        public GetCommentsRequest(int lessonId, int personId, String token)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
        }
    }

    static public class GetCommentsReply
    {
        public String stuId;
        public String className;
        public String score;
        public String comments;
        public String creatorName;

        public GetCommentsReply(
                String stuId, String className, String score, String comments, String creatorName)
        {
            this.stuId = stuId;
            this.className = className;
            this.score = score;
            this.comments = comments;
            this.creatorName = creatorName;
        }
    }

    public abstract GetCommentsReply getComments(GetCommentsRequest request)
            throws PapaHttpClientException;

    ////////////////////////////////////////////////////////////////////////////////////////
    // 获取助教对学生的评分

    static public class PostTACommentsRequest
    {
        public int lessonId;
        public int personId;
        public String score;
        public String comments;
        public String token;

        public PostTACommentsRequest(int lessonId, int personId, String token, String score, String comments)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
            this.score = score;
            this.comments = comments;
        }
    }

    public abstract void postTAComments(PostTACommentsRequest request)
            throws PapaHttpClientException;

    //////////////////////////////////////////////////////////
    // 获取推送列表


    static public class GetMessagesIDRequest
    {
        public int personId;
        public String token;

        public GetMessagesIDRequest(int personId, String token)
        {
            this.personId = personId;
            this.token = token;
        }
    }

    static public class GetMessagesIDReply
    {
        public List<String> msgIdLst;

        public GetMessagesIDReply(List<String> msgIdLst)
        {
            this.msgIdLst = msgIdLst;
        }
    }

    public abstract GetMessagesIDReply getMessagesID(GetMessagesIDRequest request)
            throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////
    // 根据推送 id 找详细信息

    static public class GetMessageByIDRequest
    {
        public String msgId;
        public String token;

        public GetMessageByIDRequest(String msgId, String token)
        {
            this.msgId = msgId;
            this.token = token;
        }
    }

    static public class GetMessageByIDReply
    {
        public Message msg;

        public GetMessageByIDReply(Message msg)
        {
            this.msg = msg;
        }
    }

    public abstract GetMessageByIDReply getMessageByID(GetMessageByIDRequest request)
            throws PapaHttpClientException;

    // どこまで叫べば位置を知れる

    //////////////////////////////////////////////////////////////////////////
    // 学生课程评价

    static public class PostStudentCommentsRequest
    {
        public int lessonId;
        public int personId;
        public String score;
        public String comments;
        public String token;

        public PostStudentCommentsRequest(int lessonId, int personId, String token, String score, String comments)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
            this.score = score;
            this.comments = comments;
        }
    }

    public abstract void postStudentComments(PostStudentCommentsRequest request)
            throws PapaHttpClientException;

    static public class GetStudentCommentsRequest
    {
        public int lessonId;
        public int personId;
        public String token;

        public GetStudentCommentsRequest(int lessonId, int personId, String token)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;
        }
    }


    static public class GetStudentCommentsReply
    {
        public String comments;
        public String score;

        public GetStudentCommentsReply(String score, String comments)
        {
            this.comments = comments;
            this.score = score;
        }
    }

    public abstract GetStudentCommentsReply getStudentComments(GetStudentCommentsRequest request)
            throws PapaHttpClientException;

    // 赤い　紅い　アカイ

    //////////////////////////////////////////////////////////////////////////
    // 学生文件

    static public class PostFileOnLessonRequest
    {
        public int lessonId;
        public int personId;
        public String token;

        public String fileType;
        public String fileName;
        public File file;

        public Media media;

        public PostFileOnLessonRequest(
                int lessonId, int personId, String token, File file, String fileName,
                String fileType, Media media)
        {
            this.lessonId = lessonId;
            this.personId = personId;
            this.token = token;

            this.fileType = fileType;
            this.fileName = fileName;
            this.file = file;
            this.media = media;
        }
    }

    static public class PostFileOnLessonReply
    {
        public String id;

        public PostFileOnLessonReply(String id)
        {
            this.id = id;
        }
    }

    public abstract PostFileOnLessonReply postFileOnLesson(PostFileOnLessonRequest request)
            throws PapaHttpClientException;


    static public class DeleteFileRequest
    {
        public String token;
        public String fileId;
        public String lessonId;
        public String personId;

        public DeleteFileRequest(String token, String lessonId, String personId, String fileId)
        {
            this.token = token;
            this.lessonId = lessonId;
            this.personId = personId;
            this.fileId = fileId;
        }
    }

    public abstract void deleteFile(DeleteFileRequest request)
            throws PapaHttpClientException;


    /////////////////////////////////////////////////////////////////////
    // 获取文件列表

    static public class GetFilesRequest
    {
        public String token;
        public String lessonId;
        public String personId;
        public File file;

        public GetFilesRequest(String token, String lessonId, String personId, File file)
        {
            this.token = token;
            this.lessonId = lessonId;
            this.personId = personId;
            this.file = file;
        }
    }

    static public class GetFilesReply
    {
        public List<Media> mediaList;

        GetFilesReply()
        {
            mediaList = new ArrayList<Media>();
        }
    }

    public abstract GetFilesReply getLessonFiles(GetFilesRequest request)
            throws PapaHttpClientException;

    /////////////////////////////////////////////////////////////////////
    // 获取文件列表

    static public class GetLessonFilesRequest
    {
        public String token;
        public String lessonId;
        public File file;

        public GetLessonFilesRequest(String token, String lessonId, File file)
        {
            this.token = token;
            this.lessonId = lessonId;
            this.file = file;
        }
    }

    static public class GetLessonFilesReply
    {
        public List<File> files;

        GetLessonFilesReply()
        {
            files = new ArrayList<File>();
        }
    }

    public abstract GetLessonFilesReply getLessonFiles(GetLessonFilesRequest request)
            throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////
    // 学生课程评价

    static public class PostAvatarRequest
    {
        public String token;
        public int personId;

        public String fileType;
        public String fileName;
        public File file;

        public PostAvatarRequest(
                int personId, String token, File file, String fileName, String fileType)
        {
            this.personId = personId;
            this.token = token;

            this.fileType = fileType;
            this.fileName = fileName;
            this.file = file;
        }
    }

    public abstract void postAvatar(PostAvatarRequest request)
            throws PapaHttpClientException;



    //////////////////////////////////////////////////////////////////////////
    // 学生签到

    static public class PostAttendance
    {
        public String token;
        public double latitude;
        public double longitude;
        public boolean locationServiceAvailable;
        public String personId;
        public String lessonId;

        public PostAttendance(
                String token, String personId, String lessonId,
                double latitude, double longitude, boolean locationServiceAvailable
        )
        {
            this.token = token;
            this.personId = personId;
            this.lessonId = lessonId;

            this.latitude = latitude;
            this.longitude = longitude;
            this.locationServiceAvailable = locationServiceAvailable;
        }
    }

    public abstract void postAttendance(PostAttendance request)
            throws PapaHttpClientException;
    public abstract void deleteAttendance(PostAttendance request)
            throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////
    // 获取聊天(互动交流)信息

    static public class ChatMessage implements Parcelable{
        public String id;
        public String senderId;
        public String senderName;
        public String title;
        public String content;
        public String status;
        public Calendar created_at;

        public ChatMessage(
                String id, String senderId, String senderName,
                String title, String content, String status, Calendar created_at)
        {
            this.id = id;
            this.senderId = senderId;
            this.senderName = senderName;
            this.title = title;
            this.content = content;
            this.status = status;
            this.created_at = created_at;

        }

        @Override
        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){
            dest.writeString(id);
            dest.writeString(senderId);
            dest.writeString(senderName);
            dest.writeString(title);
            dest.writeString(content);
            dest.writeString(status);
            dest.writeSerializable(created_at);
        }

        public static final Parcelable.Creator<ChatMessage> CREATOR
                = new Parcelable.Creator<ChatMessage>() {
            public ChatMessage createFromParcel(Parcel in) {
                return new ChatMessage(in);
            }

            public ChatMessage[] newArray(int size) {
                return new ChatMessage[size];
            }
        };

        private ChatMessage(Parcel in) {
            id = in.readString();
            senderId = in.readString();
            senderName = in.readString();
            title = in.readString();
            content = in.readString();
            status = in.readString();
            created_at = (Calendar)in.readSerializable();
        }
    }

    static public class GetChatMessageRequest
    {
        public String token;

        public GetChatMessageRequest(String token)
        {
            this.token = token;

        }
    }

    static public class GetChatMessageReply
    {
        public List<ChatMessage> list;

        public GetChatMessageReply(List<ChatMessage> list)
        {
            this.list = list;
        }

    }

    public abstract GetChatMessageReply getChatMessages(GetChatMessageRequest request)
            throws PapaHttpClientException;

    //////////////////////////////////////////////////////////////////////////
    // 标志已读

    static public class ReadChatMessageRequest
    {
        public String token;
        public String messageId;

        public ReadChatMessageRequest(String token, String messageId)
        {
            this.token = token;
            this.messageId = messageId;

        }
    }

    public abstract void readChatMessages(ReadChatMessageRequest request)
            throws PapaHttpClientException;


    //////////////////////////////////////////////////////////////////////////
    // 发送消息
    static public class PostChatMessageRequest
    {
        public String token;
        public String title;
        public String content;
        public String receiverId;

        public PostChatMessageRequest(String token, String title, String content, String receiverId)
        {
            this.token = token;
            this.title = title;
            this.content = content;
            this.receiverId = receiverId;

        }
    }

    public abstract void postChatMessages(PostChatMessageRequest request)
            throws PapaHttpClientException;


    /**
       获取相关老师的信息
    **/


    static public class GetTeachersInfoRequest
    {
        public String token;
        public String personId;

        public GetTeachersInfoRequest(String token, String personId)
        {
            this.token = token;
            this.personId = personId;
        }
    }

    static public class TeacherInfo implements Parcelable
    {
        String teacherName;
        String courseName;
        String teacherTelephone;
        String teacherMail;
        String teacherId;

        @Override
        public int describeContents(){
            return 0;
        }

        public String getTeacherName(){
            return teacherName;
        }
        public String getCourseName(){
            return courseName;
        }
        public String getTeacherTelephone(){
            return teacherTelephone;
        }
        public String getTeacherMail(){
            return teacherMail;
        }
        public String getTeacherId(){
            return teacherId;
        }

        @Override
        public String toString() {
            String s = "teacher: ";
            s += teacherName + " ";
            s += courseName + " ";
            s += teacherTelephone + " ";
            s += teacherMail + " ";
            s += teacherId + "\n";
            return s;
        }

        public TeacherInfo(
                String teacherName, String courseName, String teacherTelephone,
                String teacherMail, String teacherId) {
            this.teacherName = teacherName;
            this.courseName = courseName;
            this.teacherTelephone = teacherTelephone;
            this.teacherMail = teacherMail;
            this.teacherId = teacherId;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.teacherName);
            dest.writeString(this.courseName);
            dest.writeString(this.teacherTelephone);
            dest.writeString(this.teacherMail);
            dest.writeString(this.teacherId);
        }

        // 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口  
        public static final Parcelable.Creator<PapaDataBaseManager.TeacherInfo> CREATOR = new Parcelable.Creator<PapaDataBaseManager.TeacherInfo>() {
            @Override
            public PapaDataBaseManager.TeacherInfo createFromParcel(Parcel source) {
                // 从Parcel中读取数据，返回PapaDataBaseManager.TeacherInfo对象  
                return new PapaDataBaseManager.TeacherInfo(source.readString(),
                        source.readString() ,source.readString(), source.readString(), source.readString());
            }

            @Override
            public PapaDataBaseManager.TeacherInfo[] newArray(int size) {
                return new PapaDataBaseManager.TeacherInfo[size];
            }
        };
    }


    static public class GetTeachersInfoReply
    {
        public List<TeacherInfo> list;

        public GetTeachersInfoReply(List<TeacherInfo> list)
        {
            this.list = list;
        }
    }

    public abstract GetTeachersInfoReply getTeachersInfo(GetTeachersInfoRequest request)
            throws PapaHttpClientException;



}