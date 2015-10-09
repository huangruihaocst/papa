package com.example.huang.papa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.PapaDataBaseManager.papa.PapaDataBaseManager;

public class SignInActivity extends AppCompatActivity {

    String username;
    String password;
    int check_message = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button button_sign_in = (Button)findViewById(R.id.sign_in);
        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void check(){//1 for right, 0 for wrong password, and 2 for not registered
        EditText edit_username = (EditText)findViewById(R.id.username);
        EditText edit_password = (EditText)findViewById(R.id.password);
        username = edit_username.getText().toString();
        password = edit_password.getText().toString();

        PapaDataBaseManager papaDataBaseManager = PapaDataBaseManager.getInstance();
        if(papaDataBaseManager.signIn(username, password))
        {
            check_message = 1;
        }
        else
        {
            // ToDo: 区分到底是错误密码还是未注册
            check_message = 0;
        }
    }

    public void signIn(){
        check();
        if(check_message == 1){
            Intent intent = new Intent(SignInActivity.this,CourseActivity.class);
            Bundle data = new Bundle();
            String key_sign_in_course_1 = getString(R.string.key_sign_in_course_1);
            data.putString(key_sign_in_course_1,username);
            intent.putExtras(data);
            startActivity(intent);
        }else if(check_message == 0){
            Toast.makeText(getApplicationContext(),getString(R.string.wrong_password),Toast.LENGTH_LONG).show();
        }else if(check_message == 2){
            Toast.makeText(getApplicationContext(),getString(R.string.not_signed_up),Toast.LENGTH_LONG).show();
        }
    }
}