package com.Activities.test;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;

import com.Activities.papa.R;
import com.Activities.papa.SignInActivity;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetter;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetterReal;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetterKongBaKongKong;


/**
 * Created by shyo on 15-10-16.
 */

// see https://m.oschina.net/blog/383068
public class SignInTest extends ActivityUnitTestCase<SignInActivity>
{
    Intent mSignInIntent;
    ContextThemeWrapper context;

    public SignInTest()
    {
        super(SignInActivity.class);
    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        // see http://stackoverflow.com/questions/22364433/activityunittestcase-and-startactivity-with-actionbaractivity
        mSignInIntent = new Intent(getInstrumentation().getTargetContext(), SignInActivity.class);
        context = new ContextThemeWrapper(getInstrumentation().getTargetContext(), R.style.AppTheme);
    }

    @MediumTest
    public void testGetTelephone()
    {

        try {
            Log.i("testGetTelephone", "testGetTelephone()");

            setActivityContext(context);
            startActivity(mSignInIntent, null, null);

            SignInActivity activity = getActivity();

            activity.changeTelephoneNumberGetter
                    ((new PapaTelephoneNumberGetterKongBaKongKong()));

            final Button getTelephoneButton = (Button) activity.findViewById(R.id.get_telephone_number);
            final TextView lineEdit = (TextView) activity.findViewById(R.id.username);

            getTelephoneButton.performClick();


            assertEquals("0800092000", lineEdit.getText().toString());
        }
        catch(Exception e)
        {
            Log.e("testGetTelephone", e.getMessage());

        }
    }
}
