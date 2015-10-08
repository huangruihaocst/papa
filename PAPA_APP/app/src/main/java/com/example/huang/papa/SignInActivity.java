package com.example.huang.papa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    String username;
    String password;

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

    public Boolean check(){
        EditText edit_username = (EditText)findViewById(R.id.username);
        EditText edit_password = (EditText)findViewById(R.id.password);
        username = edit_username.getText().toString();
        password = edit_password.getText().toString();
        return true;
    }

    public void signIn(){
        if(check()){
            Intent intent = new Intent(SignInActivity.this,SemesterActivity.class);
            Bundle data = new Bundle();
            String key_sign_in_course_1 = getString(R.string.key_sign_in_course_1);
            String key_sign_in_course_2 = getString(R.string.key_sign_in_course_2);
            data.putString(key_sign_in_course_1,username);
            data.putString(key_sign_in_course_2,password);
            intent.putExtras(data);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.wrong_password),Toast.LENGTH_LONG).show();//or not registered
        }
    }
}