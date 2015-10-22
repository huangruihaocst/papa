package com.Back.PapaDataBaseManager.papa;

import com.Back.DataBaseAccess.papa.PapaDataBaseNotSuccessError;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.NetworkAccess.papa.PapaHttpClientIOErrorException;
import com.Back.NetworkAccess.papa.PapaHttpClientNot200Exception;

/**
 * Created by shyo on 15-10-22.
 */

public class PapaDataBaseManagerJiaDe extends PapaDataBaseManager
{
    // This is a 假的 Laobi!
    // PapaDataBaseManagerReal is the 真的 Laobi!

    // Announcement: You should finish this class before the discussion section.


    @Override
    public SignInReply signIn(SignInRequest signInRequest) throws PapaHttpClientException {
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
        if(signInRequest.id.equals("123") && signInRequest.pwd.equals("123"))
            return new SignInReply(1, "watashi");
        throw new PapaHttpClientNot200Exception(401);
    }
}
