package com.Activities.papa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Back.DataBaseAccess.papa.PapaDataBaseResourceNotFound;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

public class DetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    PapaDataBaseManager papaDataBaseManager;

    String experiment_name;
    String identity;
    BundleHelper bundleHelper = new BundleHelper();
    TextView user_id;
    TextView user_class;
    EditText user_grades;
    EditText user_comment;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
//        String key_to_detail = getString(R.string.key_to_detail);
//        bundleHelper = data.getParcelable(key_to_detail);
        experiment_name = bundleHelper.getExperimentName();
        identity = bundleHelper.getIdentity();
        papaDataBaseManager = bundleHelper.getPapaDataBaseManager();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(experiment_name);
        setSupportActionBar(toolbar);

        user_id = (TextView)findViewById(R.id.user_id);
        user_class = (TextView)findViewById(R.id.user_class);
        user_grades = (EditText)findViewById(R.id.user_grade);
        user_comment = (EditText)findViewById(R.id.user_comment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_detail);
        if(identity.equals("student")){
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_grades.setEnabled(true);
                user_grades.setFocusable(true);
                user_comment.setEnabled(true);
                user_comment.setFocusable(true);
                Snackbar.make(view,getString(R.string.now_editable), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(bundleHelper.getIdentity().equals("teacher_assistant")){
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_upload_history);
            item.setVisible(false);
        }
        setHeaderView(navigationView);

        getComment();

        user_grades.setEnabled(false);
        user_comment.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        if(identity.equals("teacher_assistant")){
            MenuItem item = menu.findItem(R.id.action_generate_QR_code);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
            builder.setTitle(getString(R.string.select_type)).setItems(R.array.upload_type, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){//camera

                    }else if(which == 1){//video

                    }else if(which == 2){//gallery
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }else if(id == R.id.action_student_information){
            return true;
        }else if(id == R.id.action_generate_QR_code){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            Intent intent = new Intent(DetailActivity.this,FavoriteActivity.class);
            Bundle data = new Bundle();
            String key_to_favorite = getString(R.string.key_to_favorite);
            data.putParcelable(key_to_favorite,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_experiment_history) {
            Intent intent = new Intent(DetailActivity.this,ExperimentHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_experiment_history = getString(R.string.key_to_experiment_history);
            data.putParcelable(key_to_experiment_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_upload_history) {
            Intent intent = new Intent(DetailActivity.this,UploadHistoryActivity.class);
            Bundle data = new Bundle();
            String key_to_upload_history = getString(R.string.key_to_upload_history);
            data.putParcelable(key_to_upload_history,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(DetailActivity.this,EditProfileActivity.class);
            Bundle data = new Bundle();
            String key_to_edit_profile = getString(R.string.key_to_edit_profile);
            data.putParcelable(key_to_edit_profile,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(DetailActivity.this,HelpActivity.class);
            Bundle data = new Bundle();
            String key_to_help = getString(R.string.key_to_help);
            data.putParcelable(key_to_help,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DetailActivity.this,SettingsActivity.class);
            Bundle data = new Bundle();
            String key_to_settings = getString(R.string.key_to_settings);
            data.putParcelable(key_to_settings,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        } else if(id == R.id.nav_notification){
            Intent intent = new Intent(DetailActivity.this,NotificationActivity.class);
            Bundle data = new Bundle();
            String key_to_notification = getString(R.string.key_to_notification);
            data.putParcelable(key_to_notification,bundleHelper);
            intent.putExtras(data);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setHeaderView(NavigationView navigationView){
        LinearLayout linearLayout = (LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_course);
        TextView username_label = (TextView)linearLayout.findViewById(R.id.username_label);
        TextView mail_label = (TextView)findViewById(R.id.mail_label);
        ImageView image_label = (ImageView)findViewById(R.id.image_label);
    }

    //call this in another thread
    private void getComment(){
        new GetCommentTask(this).execute(
                new PapaDataBaseManager.GetCommentsRequest(
                        bundleHelper.getExperimentId(),
                        bundleHelper.getStudentId(),
                        bundleHelper.getToken()
                )
        );
    }

    private void setComment(PapaDataBaseManager.GetCommentsReply reply)
    {
        user_id.setText(reply.stuId);
        user_class.setText(reply.className);

        user_grades.setText(reply.score);
        user_comment.setText(reply.comments);

    }

    class GetCommentTask extends
            AsyncTask<PapaDataBaseManager.GetCommentsRequest, Exception, PapaDataBaseManager.GetCommentsReply> {
        ProgressDialog proDialog;

        public GetCommentTask(Context context) {
            proDialog = new ProgressDialog(context, 0);
            proDialog.setMessage("稍等喵 =w=");
            proDialog.setCancelable(false);
            proDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            // UI

            proDialog.show();
        }

        @Override
        protected PapaDataBaseManager.GetCommentsReply doInBackground
                (PapaDataBaseManager.GetCommentsRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getComments(params[0]);
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {

            if(e[0] instanceof PapaDataBaseResourceNotFound)
                onBackPressed();

            // if(e)
            // UI
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.GetCommentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setComment(rlt);
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
        Toast.makeText(getApplicationContext(),"123",Toast.LENGTH_LONG).show();
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if(uri == null) {
            // TODO: perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection,null,null,null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
