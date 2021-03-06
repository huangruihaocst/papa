package com.Back.PapaDataBaseManager.papa;

import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.NetworkAccess.papa.PapaHttpClientIOErrorException;
import com.Back.NetworkAccess.papa.PapaHttpClientNot200Exception;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by shyo on 15-10-22.
 */

public class PapaDataBaseManagerJiaDe extends PapaDataBaseManager
{
    // This is a 假的 Laobi!
    // PapaDataBaseManagerReal is the 真的 Laobi!

    // Announcement: You should finish this class before the discussion section.

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
        r.semester.put(1, "2015 Fall");
        r.semester.put(2, "2014 Spring");
        r.semester.put(3, "2014 Fall");
        r.semester.put(4, "2013 Spring");
        r.semester.put(100, "2013 Fall");
        return r;
    }

    @Override
    public GetCourseReply getStuCourse(GetCourseRequest request) throws PapaHttpClientException {
        kaSi();
        GetCourseReply r = new GetCourseReply();
        r.course.add(new AbstractMap.SimpleEntry<>(2, "模拟电路实验"));
        return r;
    }

    @Override
    public GetCourseReply getTACourse(GetCourseRequest request) throws PapaHttpClientException {
        kaSi();
        GetCourseReply r = new GetCourseReply();
        r.course.add(new AbstractMap.SimpleEntry<>(1, "数字电路实验"));
        return r;
    }

    @Override
    public GetLessonReply getLesson(GetLessonRequest request) throws PapaHttpClientException {
        kaSi();

        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        if(c.get(Calendar.SECOND) % 2 == 1)
            throw new PapaHttpClientIOErrorException();

        GetLessonReply r = new GetLessonReply();
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
    public GetUsrInfoReply getUsrInfo(GetUsrInfoRequest request) throws PapaHttpClientException {
        kaSi();
        if(request.token.equals("watashi"))
            return new GetUsrInfoReply(1, "watashi", "watashi [at] gmail [dot] com", "0800092000");
        throw new PapaDataBaseNotSuccessError();
    }
}
