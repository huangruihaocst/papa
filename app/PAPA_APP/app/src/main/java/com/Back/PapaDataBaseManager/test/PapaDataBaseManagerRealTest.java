package com.Back.PapaDataBaseManager.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.Activities.papa.message.Message;
import com.Back.NetworkAccess.papa.PapaApacheHttpClient;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerReal;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shyo on 15-11-3.
 */
public class PapaDataBaseManagerRealTest extends InstrumentationTestCase {
    static final String Tag = "PapaDataBaseManagerRealTest";

    public void test1() throws Exception {
        PapaDataBaseManager papaDataBaseManager = new PapaDataBaseManagerReal();

        PapaDataBaseManager.SignInReply r = papaDataBaseManager.signIn(
                new PapaDataBaseManager.SignInRequest("444", "123")
        );

        int personId = r.personId;
        String token = r.token;

        List lst = papaDataBaseManager.getMessagesID(
                        new PapaDataBaseManager.GetMessagesIDRequest(personId, token)
        ).msgIdLst;

        assertTrue(lst.size() >= 0);

        Log.i(Tag, lst.toString());

    }


    public void test2() throws Exception {
        PapaDataBaseManager papaDataBaseManager = new PapaDataBaseManagerReal();

        PapaDataBaseManager.SignInReply r = papaDataBaseManager.signIn(
                new PapaDataBaseManager.SignInRequest("444", "123")
        );

        String msgId = "1";
        String token = r.token;

        Message msg = papaDataBaseManager.getMessageByID(
                new PapaDataBaseManager.GetMessageByIDRequest(msgId, token)
        ).msg;

        assertNotNull(msg);

        Log.i(
                Tag,
                msg.getId() + " " + msg.getTitle() + " " + msg.getDeadline().toString() + " " +
                        msg.getContent() + " " + msg.getCreatorName() + " " + msg.getCourseName() +
                        " " + msg.getType()
        );

    }



    public void test3() throws Exception {
        PapaDataBaseManager papaDataBaseManager = new PapaDataBaseManagerReal();

        PapaDataBaseManager.SignInReply r = papaDataBaseManager.signIn(
                new PapaDataBaseManager.SignInRequest("444", "123")
        );

        String msgId = "2";
        String token = r.token;

        Message msg = papaDataBaseManager.getMessageByID(
                new PapaDataBaseManager.GetMessageByIDRequest(msgId, token)
        ).msg;

        assertNotNull(msg);

        Log.i(
                Tag,
                msg.getId() + " " + msg.getTitle() + " " + msg.getDeadline().toString() + " " +
                        msg.getContent() + " " + msg.getCreatorName() + " " + msg.getCourseName() +
                        " " + msg.getType()
        );

    }
}