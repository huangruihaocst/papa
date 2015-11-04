package com.Activities.papa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.Fragments.papa.ExperimentInformationFragment;
import com.Fragments.papa.ExperimentResultFragment;
import com.Fragments.papa.GradesFragment;
import com.Fragments.papa.StudentsFragment;

public class ExperimentDetailActivity extends AppCompatActivity {

    BundleHelper bundleHelper;
    String identity;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String key_experiment_experiment_detail = getString(R.string.key_experiment_experiment_detail);
        bundleHelper = data.getParcelable(key_experiment_experiment_detail);

        setContentView(R.layout.activity_experiment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(bundleHelper.getCourseName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        identity = bundleHelper.getIdentity();

        tabLayout = (TabLayout)findViewById(R.id.option_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.experiment_information)));
        if(identity.equals("teacher_assistant"))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.view_students)));
        else if(identity.equals("student"))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.view_grades)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.experiment_result)));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.options_view_pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new ExperimentDetailActivity.OptionsAdapter(getSupportFragmentManager(),3,identity));
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.experiment_detail, menu);
        MenuItem item = menu.getItem(0);
        if(identity.equals("teacher_assistant"))item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_comment) {
            Intent intent = new Intent(ExperimentDetailActivity.this,CommentActivity.class);
            Bundle data = new Bundle();
            String key_experiment_detail_comment = getString(R.string.key_experiment_detail_comment);
            data.putParcelable(key_experiment_detail_comment,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class OptionsAdapter extends FragmentStatePagerAdapter {
        int tabs_amount;
        String identity;

        public OptionsAdapter(FragmentManager fm) {
            super(fm);
        }
        public OptionsAdapter(FragmentManager fm, int count, String identity) {
            this(fm);
            this.tabs_amount = count;
            this.identity = identity;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            switch (position){
                case 0:
                    // newInstance("1","2") 是搞毛阿!!!
                    // 你这也搞得太简陋了吧

                    fragment = ExperimentInformationFragment.newInstance(bundleHelper);
                    break;
                case 1:
                    if (identity.equals("teacher_assistant")){
                        fragment = StudentsFragment.newInstance(bundleHelper);
                    }else if(identity.equals("student")){
                        fragment = GradesFragment.newInstance(bundleHelper);
                    }
                    break;
                case 2:
                    fragment = ExperimentResultFragment.newInstance("1", "2");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabs_amount;
        }

    }

}
