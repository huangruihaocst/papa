package com.Activities.papa.send_message;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;

public class SentListActivity extends AppCompatActivity {

    ListView list_sent;
    BundleHelper bundleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_list);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_to_send_message = getString(R.string.key_to_sent_list);
        bundleHelper = data.getParcelable(key_to_send_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.send_message));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_sent = (ListView)findViewById(R.id.list_sent);
        setSentMessages();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentListActivity.this,NewMessageActivity.class);
                Bundle data = new Bundle();
                String key_sent_list_new_message = getString(R.string.key_sent_list_new_message);
                data.putParcelable(key_sent_list_new_message,bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }

    private void setSentMessages(){




        //TODO: get sent messages here. You may like to do it in a new thread. Extends BaseAdapter.
    }
}
