package com.Back.PapaDataBaseManager.papa;

import com.Activities.papa.message.Message;
import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.NetworkAccess.papa.PapaHttpClientIOErrorException;
import com.Back.NetworkAccess.papa.PapaHttpClientNot200Exception;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shyo on 15-10-22.
 */

public class PapaDataBaseManagerJiaDe extends PapaDataBaseManager
{
    // This is a 假的 Laobi!
    // PapaDataBaseManagerReal is the 真的 Laobi!

    // Announcement: You should finish this class before the discussion section.

    public PapaDataBaseManagerJiaDe()
    {
    }

    // @Mato_No1 卡死
    private void kaSi()
    {
        synchronized(this)
        {
            try {
                wait(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException {
        kaSi();
        if(signInRequest.id.equals("123") && signInRequest.pwd.equals("123"))
            return new SignInReply(1, "watashi");
        throw new PapaHttpClientNot200Exception(401);
    }

    @Override
    public SemesterReply getSemester() throws PapaHttpClientException {
        kaSi();
        SemesterReply r = new SemesterReply();
        r.semester.add(new AbstractMap.SimpleEntry<>(0, "2015 Fall"));
        r.semester.add(new AbstractMap.SimpleEntry<>(2, "2014 Spring"));
        r.semester.add(new AbstractMap.SimpleEntry<>(4, "2014 Fall"));
        r.semester.add(new AbstractMap.SimpleEntry<>(6, "2013 Spring"));
        r.semester.add(new AbstractMap.SimpleEntry<>(100, "2013 Fall"));
        return r;
    }

    @Override
    public CourseReply getStuCourse(CourseRequest request) throws PapaHttpClientException {
        kaSi();
        CourseReply r = new CourseReply();
        r.course.add(new AbstractMap.SimpleEntry<>(2, "模拟电路实验"));
        return r;
    }

    @Override
    public CourseReply getTACourse(CourseRequest request) throws PapaHttpClientException {
        kaSi();
        CourseReply r = new CourseReply();
        if(request.semesterId == 2)
            r.course.add(new AbstractMap.SimpleEntry<>(1, "数字电路实验"));
        if(request.semesterId == 0)
            r.course.add(new AbstractMap.SimpleEntry<>(10, "数字电路实验 2"));
        if(request.semesterId == 4)
            r.course.add(new AbstractMap.SimpleEntry<>(100, "数字电路实验 3"));
        if(request.semesterId == 6)
            r.course.add(new AbstractMap.SimpleEntry<>(1000, "数字电路实验 4"));
        return r;
    }

    @Override
    public LessonReply getLesson(LessonRequest request) throws PapaHttpClientException {
        kaSi();

        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        if(c.get(Calendar.SECOND) % 2 == 1)
            throw new PapaHttpClientIOErrorException();

        LessonReply r = new LessonReply();
        if(request.courseId == 1)
        {
            r.lesson.add(new AbstractMap.SimpleEntry<>(1, "门电路实验"));
            r.lesson.add(new AbstractMap.SimpleEntry<>(2, "触发器实验"));
            return r;
        }
        if(request.courseId == 2)
        {
            r.lesson.add(new AbstractMap.SimpleEntry<>(3, "CMOS 反相电路"));
            r.lesson.add(new AbstractMap.SimpleEntry<>(4, "ACDC 转换"));
            return r;
        }


        throw new PapaHttpClientNot200Exception(401);
    }

    @Override
    public UsrInfoReply getUsrInfo(UsrInfoRequest request) throws PapaHttpClientException {
        kaSi();
        if(request.token.equals("watashi"))
            return new UsrInfoReply(1, "watashi", "watashi [at] gmail [dot] com", "0800092000");
        throw new PapaDataBaseNotSuccessError();
    }

    @Override
    public StudentsReply getStudents(StudentsRequest request) throws PapaHttpClientException {
        kaSi();
        StudentsReply r = new StudentsReply();
        r.students.add(new AbstractMap.SimpleEntry<>(1, "博麗 　霊夢"));
        r.students.add(new AbstractMap.SimpleEntry<>(2, "霧雨　魔理沙"));
        r.students.add(new AbstractMap.SimpleEntry<>(3, "十六夜　咲夜"));
        r.students.add(new AbstractMap.SimpleEntry<>(4, "チルノ"));
        return r;
    }

    static String zero = "0";
    static String zeroComment = "Oh, I remember you. You have got a zer~o~.";

    @Override
    public GetCommentsReply getComments(GetCommentsRequest request) throws
            PapaHttpClientException {
        return new GetCommentsReply(
                "(stuId) 0800092000", "LaoBi's class",
                zero, zeroComment
        );
    }

    @Override
    public void postTAComments(PostTACommentsRequest request) throws PapaHttpClientException {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        if(c.get(Calendar.SECOND) % 2 == 1)
            throw new PapaHttpClientIOErrorException();
        zero = request.score;
        zeroComment = request.comments;
    }

    @Override
    public GetLessonInfoReply getLessonInfo(GetLessonInfoRequest request) throws
            PapaHttpClientException {
        return new GetLessonInfoReply("watashi", "kyou", "ashita", "koko");
    }

    @Override
    public GetMessagesIDReply getMessagesID(GetMessagesIDRequest request)
            throws PapaHttpClientException
    {
        ArrayList<String> ans = new ArrayList();
        ans.add("1");
        ans.add("2");
        ans.add("3");
        ans.add("4");
        ans.add("5");

        return new GetMessagesIDReply(ans);
    }

    @Override
    public GetMessageByIDReply getMessageByID(GetMessageByIDRequest request)
            throws PapaHttpClientException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 20);
        return new GetMessageByIDReply(
                new Message(
                        request.msgId, "Homework Assignment #" + request.msgId, "homework",
                        "You should finish homework" +
                                "assignment #2 before the discussion section",
                        calendar,
                        "University Physics II",
                        "Laobi"
                )
        );
    }

    @Override
    public void postStudentComments(PostStudentCommentsRequest request) throws PapaHttpClientException {

    }
}
