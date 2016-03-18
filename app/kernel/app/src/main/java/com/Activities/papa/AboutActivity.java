package com.Activities.papa;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.about));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.feedback_email));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
                startActivity(Intent.createChooser(intent, getString(R.string.feedback)));
            }
        });

        ListView about = (ListView)findViewById(R.id.about_list);
        ArrayList<String> items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.about)));
        String version = "";
        int verCode = 0;
        try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            verCode = pInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.getMessage();
        }
        //magic number is the index of version information in the about string array
        items.set(2, getResources().getStringArray(R.array.about)[2] + version + " " + String.valueOf(verCode));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        about.setAdapter(adapter);
        about.setItemsCanFocus(true);
    }

}
