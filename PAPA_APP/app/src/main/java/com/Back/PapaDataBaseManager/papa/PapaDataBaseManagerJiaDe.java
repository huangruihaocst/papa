package com.Back.PapaDataBaseManager.papa;

import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.NetworkAccess.papa.PapaHttpClientNot200Exception;

/**
 * Created by shyo on 15-10-22.
 */

public class PapaDataBaseManagerJiaDe extends PapaDataBaseManager
{
    // This is a 假的 Laobi!
    // PapaDataBaseManagerReal is the 真的 Laobi!

    // Announcement: You should finish this class before the discussion section.

    // @Mato_No1
    private void kaSi()
    {
        synchronized(this)
        {
            try {
                wait(1000);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
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
        return null;
    }

    @Override
    public GetCourseReply getTACourse(GetCourseRequest request) throws PapaHttpClientException {
        return null;
    }
}
