package com.Activities.papa.profile;

import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Activities.papa.SignInActivity;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Fragments.papa.course.CourseFragment;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER_SELECT = 999;

    BundleHelper bundleHelper;

    PapaDataBaseManager.UsrInfo usrInfo;

    EditText edit_username;
    EditText edit_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        bundleHelper = data.getParcelable(getString(R.string.key_to_edit_profile));

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        edit_username = (EditText)findViewById(R.id.edit_username);
        edit_mail = (EditText)findViewById(R.id.edit_mail);

        Button button_log_out = (Button)findViewById(R.id.log_out);
        button_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage(R.string.confirm_log_out);
                builder.setPositiveButton(R.string.confirm_log_out_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                        Bundle data = new Bundle();
                        String key_to_sign_in = getString(R.string.key_to_sign_in);
                        data.putParcelable(key_to_sign_in, new BundleHelper());//a new bundleHelper
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.confirm_log_out_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Button button_change_password = (Button)findViewById(R.id.button_change_password);
        Button button_change_photo = (Button)findViewById(R.id.button_change_photo);

        usrInfo = null;
        setProfile();

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View rootView = inflater.inflate(R.layout.set_password_dialog, null);
                builder.setView(rootView);
                final EditText edit_text_new_password = (EditText)rootView.findViewById(R.id.new_password);
                final EditText edit_text_password_double_check = (EditText)rootView.findViewById(R.id.password_double_check);
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button button_positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button button_negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        button_positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String new_password = edit_text_new_password.getText().toString();
                                String password_double_check = edit_text_password_double_check.getText().toString();
                                if(new_password.length() < 6 || password_double_check.length() < 6){
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.password_too_short),
                                            Toast.LENGTH_LONG).show();
                                }else if(!new_password.equals(password_double_check)){
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.error_double_check),
                                            Toast.LENGTH_LONG).show();
                                }else{
                                    //TODO:upload new password
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.change_password_success),
                                            Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        button_negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        button_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICKER_SELECT);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT
                && resultCode == Activity.RESULT_OK) {
            String path = getPathFromCameraData(data, ProfileActivity.this);
            Log.i("PICTURE", "Path: " + path);
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    //TODO:change the photo of the user, meanwhile change the one in the navigation drawer
                }
            }
        }
    }

    public static String getPathFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void setProfile(){
        new GetUsrInfoTask(this).execute(new PapaDataBaseManager.UsrInfoRequest(
                bundleHelper.getId(),
                bundleHelper.getToken()
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            usrInfo.usrName = edit_username.getText().toString();
            usrInfo.mail = edit_mail.getText().toString();


            new PutUsrInfoTask(this).execute(new PapaDataBaseManager.PutUsrInfoRequest(
                    bundleHelper.getId(),
                    bundleHelper.getToken(),
                    usrInfo
            ));


        }

        return super.onOptionsItemSelected(item);
    }

    class GetUsrInfoTask extends
            AsyncTask<PapaDataBaseManager.UsrInfoRequest, Exception,
                    PapaDataBaseManager.UsrInfoReply> {
        ProgressDialog proDialog;

        public GetUsrInfoTask(Context context) {
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
        protected PapaDataBaseManager.UsrInfoReply doInBackground
                (PapaDataBaseManager.UsrInfoRequest... params) {
            // 在后台
            try {
                return BundleHelper.getPapaDataBaseManager().getUsrInfo(params[0]);
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.UsrInfoReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setUsrInfo(rlt);
        }
    }

    private void setUsrInfo(PapaDataBaseManager.UsrInfoReply reply)
    {
        usrInfo = reply.usrInfo;

        edit_username.setText(usrInfo.usrName);
        edit_mail.setText(usrInfo.mail);
    }

    class PutUsrInfoTask extends
            AsyncTask<PapaDataBaseManager.PutUsrInfoRequest, Exception,
                    Boolean> {
        ProgressDialog proDialog;

        public PutUsrInfoTask(Context context) {
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
        protected Boolean doInBackground
                (PapaDataBaseManager.PutUsrInfoRequest... params) {
            // 在后台
            try {
                BundleHelper.getPapaDataBaseManager().putUsrInfo(params[0]);
                return true;
            } catch (PapaHttpClientException e) {
                publishProgress(e);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            e[0].printStackTrace();
            Toast.makeText(getApplicationContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            // UI

            proDialog.dismiss();
        }
    }

}
