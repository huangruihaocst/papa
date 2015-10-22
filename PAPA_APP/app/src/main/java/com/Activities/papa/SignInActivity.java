package com.Activities.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerJiaDe;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerReal;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetter;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetterKongBaKongKong;
import com.TelephoneInfoManager.papa.PapaTelephoneNumberGetterReal;

public class SignInActivity extends AppCompatActivity {
    String username;
    String password;
    BundleHelper bundleHelper = new BundleHelper();

    // 采用何种方式获取电话
    PapaTelephoneNumberGetter telephoneNumberGetter;

    // 使用何种数据库获取数据
    PapaDataBaseManager papaDataBaseManager;

    // Usr, pwd EditText Widget, Buttons
    EditText edit_username;
    EditText edit_password;
    Button button_sign_in;
    Button button_get_telephone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        String key_enter_sign_in = getString(R.string.key_enter_sign_in);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(key_enter_sign_in);

        edit_username = (EditText)findViewById(R.id.username);
        edit_password = (EditText)findViewById(R.id.password);

        // telephoneNumberGetter = bundleHelper.getPapaTelephoneNumberGetter();

        button_sign_in = (Button)findViewById(R.id.sign_in);
        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        button_get_telephone_number = (Button)findViewById(R.id.get_telephone_number);
        button_get_telephone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String telephone_number = getTelephoneNumber();
                    edit_username.setText(telephone_number);
                } catch (PapaTelephoneNumberGetter.cannotGetTelephoneNumberException e){
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_get_telephone_num), Toast.LENGTH_LONG).show();
                }
            }
        });


        // 默认的方法获取电话
        this.telephoneNumberGetter = new PapaTelephoneNumberGetterReal();
        this.papaDataBaseManager = new PapaDataBaseManagerJiaDe();
    }

    // 更改获取电话的方法
    public void changeTelephoneNumberGetter(PapaTelephoneNumberGetter p)
    {
        if(p == null) return;
        this.telephoneNumberGetter = p;
    }

    // 获取电话号码
    private String getTelephoneNumber()
            throws PapaTelephoneNumberGetter.cannotGetTelephoneNumberException
    {
        return this.telephoneNumberGetter.getTelephoneNumber(getBaseContext());
    }

    // 登录部分, 异步
    public void processReply(PapaDataBaseManager.SignInReply reply)
    {
        Intent intent = new Intent(SignInActivity.this,CourseActivity.class);
        Bundle data = new Bundle();
        String key_sign_in_course = getString(R.string.key_sign_in_course);

        bundleHelper.setId(reply.personId);
        bundleHelper.setToken(reply.token);

        data.putParcelable(key_sign_in_course,bundleHelper);
        intent.putExtras(data);
        startActivity(intent);

        /*
            Toast.makeText(getApplicationContext(),getString(R.string.wrong_password),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),getString(R.string.not_signed_up),Toast.LENGTH_LONG).show();
        */
    }

    private void signIn()
    {
        EditText edit_username = (EditText)findViewById(R.id.username);
        EditText edit_password = (EditText)findViewById(R.id.password);
        username = edit_username.getText().toString();
        password = edit_password.getText().toString();

        Task task = new Task(this);    // 实例化抽象AsyncTask
        task.execute(new PapaDataBaseManager.SignInRequest(username, password));    // 调用AsyncTask，传入url参数
    }

    class Task extends AsyncTask
            <PapaDataBaseManager.SignInRequest, Exception, PapaDataBaseManager.SignInReply>
    {
        ProgressDialog proDialog;

        public Task(Context context) {
            proDialog = new ProgressDialog(context, 0);
            proDialog.setMessage("稍等喵 =w=");
            proDialog.show();
            proDialog.setCancelable(false);
            proDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute(){
            // UI
        }

        @Override
        protected PapaDataBaseManager.SignInReply doInBackground
                (PapaDataBaseManager.SignInRequest... params)
        {
            // 在后台
            try {
                return papaDataBaseManager.signIn(params[0]);
            } catch(PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e)
        {
            // UI
            Log.e("SignInAct", e[0].getMessage().toString());

            Toast.makeText(getApplicationContext(), e[0].getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.SignInReply rlt) {
            // UI

            proDialog.dismiss();
            if(rlt != null) processReply(rlt);
        }
    }
}