package com.Activities.papa;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.feedback_email));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
                startActivity(Intent.createChooser(intent, getString(R.string.feedback)));
            }
        });

        ListView about = (ListView)findViewById(R.id.about_list);
        ArrayList<String> items = new ArrayList<>();
        String version = "";
        int verCode = 0;
        try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            verCode = pInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.getMessage();
        }
        items.add(getString(R.string.about_name));
        items.add(getString(R.string.about_author));
        items.add(getString(R.string.about_version) + version + " " + String.valueOf(verCode));
        items.add(getString(R.string.about_contact));
        items.add(getString(R.string.about_copyright));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        about.setAdapter(adapter);
        about.setItemsCanFocus(true);
    }

}
