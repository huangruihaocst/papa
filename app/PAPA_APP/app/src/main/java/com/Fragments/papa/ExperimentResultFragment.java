package com.Fragments.papa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.DetailActivity;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Fragments.papa.student.StudentsListAdapter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


// 赤より紅い夢！！！！！
// 比赤色更加鲜红的梦想!!!!!!!

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExperimentResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExperimentResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentResultFragment extends Fragment {

    private static final String TAG = "ExperimentResultFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BUNDLE_HELPER = "bundleHelper";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Code for our image picker select action.
    private static final int IMAGE_PICKER_SELECT = 999;
    private static final int CAPTURE_IMAGE = 998;
    private static final int CAPTURE_VIDEO = 997;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    private BundleHelper bundleHelper;
    private Uri fileUri;

    // Reference to our image view we will use
//    private ImageView selectedImage;
//    private VideoView selectedVideo;
    GridView gridView_image;
    byte[] bytes;

    private int[] imageId = {
            R.drawable.ic_file_upload_black_24dp,
            R.drawable.ic_history_black_24dp,
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_notifications_black_24dp,
    };
    private ArrayList<Bitmap> bitmapArrayList;
    private ArrayList<String> pathArrayList;
    private ArrayList<Integer> isImageArrayList;//1 for image, 0 for video
    private ImageAdapter imageAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ExperimentResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperimentResultFragment newInstance(BundleHelper bundleHelper, String param2) {
        ExperimentResultFragment fragment = new ExperimentResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BUNDLE_HELPER, bundleHelper);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ExperimentResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleHelper = getArguments().getParcelable(ARG_BUNDLE_HELPER);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bitmapArrayList = new ArrayList<>();
        pathArrayList = new ArrayList<>();
        isImageArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_experiment_result, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.fab_upload_result);
//        selectedImage = (ImageView)rootView.findViewById(R.id.selected_image);
//        selectedVideo = (VideoView)rootView.findViewById(R.id.selected_video);
        gridView_image = (GridView)rootView.findViewById(R.id.gridView_image);
        imageAdapter = new ImageAdapter(getContext());
        gridView_image.setAdapter(imageAdapter);

        for(int i = 0;i < imageId.length;i ++){
            bitmapArrayList.add(BitmapFactory.decodeResource(getResources(), imageId[i]));
            pathArrayList.add("");
            isImageArrayList.add(1);
        }

        gridView_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File(pathArrayList.get(position));
                if (isImageArrayList.get(position) == 1) {
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                }
                startActivity(intent);
            }
        });

        gridView_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundleHelper.getIdentity() == BundleHelper.Identity.teacher_assistant){
                    AlertDialog.Builder builder = new AlertDialog().Builder(getActivity());
                    builder.setTitle(getString(R.string.select_student));

                    // String students[];
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.select_type)).setItems(R.array.upload_type, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0){//camera
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to commit the image
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                                // start the image capture Intent
                                startActivityForResult(intent, CAPTURE_IMAGE);
                            }else if(which == 1){//video
                                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to commit the video
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high

                                // start the Video Capture Intent
                                startActivityForResult(intent, CAPTURE_VIDEO);
                            }else if(which == 2){//gallery
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("*/*");
                                startActivityForResult(intent, IMAGE_PICKER_SELECT);
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT
                && resultCode == Activity.RESULT_OK) {
            String path = getPathFromCameraData(data, getActivity());
            Log.i("PICTURE", "Path: " + path);
            if (path != null) {
                File file = new File(path);
                if(file.exists()){
                    Uri returnUri = data.getData();
                    String mimeType = getContext().getContentResolver().getType(returnUri);
                    if(mimeType != null){
                        if(mimeType.contains("video")){
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail
                                    (path,MediaStore.Video.Thumbnails.MINI_KIND);
                            bitmapArrayList.add(thumbnail);
                            pathArrayList.add(path);
                            isImageArrayList.add(0);
                            imageAdapter.notifyDataSetChanged();

                            new UploadTask(getContext()).execute(
                                    new PapaDataBaseManager.PostFileOnLessonAsStudentRequest(
                                            bundleHelper.getExperimentId(),
                                            bundleHelper.getStudentId(),
                                            bundleHelper.getToken(),
                                            file,
                                            file.getName(),
                                            "video"
                                    )
                            );

                        }else if(mimeType.contains("image")){
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    selectedImage.setImageBitmap(bitmap);
                            bitmap = ThumbnailUtils.extractThumbnail(bitmap,200,200);
                            bitmapArrayList.add(bitmap);
                            pathArrayList.add(path);
                            isImageArrayList.add(1);
                            imageAdapter.notifyDataSetChanged();
                            // byte[] bytes = toByteArray(file);

                            new UploadTask(getContext()).execute(
                                    new PapaDataBaseManager.PostFileOnLessonAsStudentRequest(
                                            bundleHelper.getExperimentId(),
                                            bundleHelper.getStudentId(),
                                            bundleHelper.getToken(),
                                            file,
                                            file.getName(),
                                            "image"
                                    )
                            );
                        }else{
                            Toast.makeText(getContext(),getString(R.string.invalid_file),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(),getString(R.string.no_media),Toast.LENGTH_LONG).show();
                    }

                }
            }
        }else if(requestCode == CAPTURE_IMAGE){
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Log.i("PICTURE", "Image saved to:\n" + fileUri.toString());
                String path = fileUri.getPath();
                if (path != null) {
                    File file = new File(path);
                    if(file.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        selectedImage.setImageBitmap(bitmap);
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap,200,200);
                        bitmapArrayList.add(bitmap);
                        pathArrayList.add(path);
                        isImageArrayList.add(1);
                        imageAdapter.notifyDataSetChanged();
                        // byte[] bytes = toByteArray(file);

                        new UploadTask(getContext()).execute(
                                new PapaDataBaseManager.PostFileOnLessonAsStudentRequest(
                                        bundleHelper.getExperimentId(),
                                        bundleHelper.getStudentId(),
                                        bundleHelper.getToken(),
                                        file,
                                        file.getName(),
                                        "image"
                                )
                        );
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }else if(requestCode == CAPTURE_VIDEO){
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Log.i("PICTURE", "Video saved to:\n" + data.getData());
                String path = fileUri.getPath();
                if (path != null) {
                    File file = new File(path);
                    if(file.exists()){
//                        selectedVideo.setVideoPath(path);
//                        selectedVideo.start();
                        // byte[] bytes = toByteArray(file);
                        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail
                                (path,MediaStore.Video.Thumbnails.MINI_KIND);
                        bitmapArrayList.add(thumbnail);
                        pathArrayList.add(path);
                        isImageArrayList.add(0);
                        imageAdapter.notifyDataSetChanged();

                        new UploadTask(getContext()).execute(
                                new PapaDataBaseManager.PostFileOnLessonAsStudentRequest(
                                        bundleHelper.getExperimentId(),
                                        bundleHelper.getStudentId(),
                                        bundleHelper.getToken(),
                                        file,
                                        file.getName(),
                                        "video"
                                )
                        );
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    void getStudents() {
        Log.i(TAG, bundleHelper.getCourseId() + " " + bundleHelper.getToken());
        new GetStudentsTask(getContext()).execute(new PapaDataBaseManager.StudentsRequest(
                        bundleHelper.getCourseId(),
                        bundleHelper.getToken())
        );
    }

    void setStudents(PapaDataBaseManager.StudentsReply rlt)
    {
        
    }

    class GetStudentsTask extends
            AsyncTask<PapaDataBaseManager.StudentsRequest, Exception, PapaDataBaseManager.StudentsReply> {
        ProgressDialog proDialog;

        public GetStudentsTask(Context context) {
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
        protected PapaDataBaseManager.StudentsReply doInBackground
                (PapaDataBaseManager.StudentsRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getStudents(params[0]);
            } catch (PapaHttpClientException e) {
                publishProgress(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            Toast.makeText(getContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.StudentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setStudents(rlt);
        }
    }

    class UploadTask extends AsyncTask
            <PapaDataBaseManager.PostFileOnLessonAsStudentRequest,
                    Exception, Boolean>
    {
        ProgressDialog proDialog;

        public UploadTask(Context context) {
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
        protected Boolean doInBackground
                (PapaDataBaseManager.PostFileOnLessonAsStudentRequest... params)
        {
            // 在后台
            try {
                bundleHelper.getPapaDataBaseManager().postFileOnLessonAsStudent(params[0]);
                return true;
            } catch(PapaHttpClientException e) {
                publishProgress(e);
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Exception... e) {
            // UI
            Log.e(TAG, e[0].getMessage());
            Toast.makeText(getContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean rlt) {
            // UI

            proDialog.dismiss();
            if(rlt != false)
                Toast.makeText(getContext(), "上传好了喵", Toast.LENGTH_SHORT).show();
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

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PAPA");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

//    public byte[] toByteArray(File file){
//        int size = (int) file.length();
//        byte[] bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return bytes;
//    }

    public class ImageAdapter extends BaseAdapter{

        private Context context;

        @Override
        public int getCount() {
            return bitmapArrayList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setPadding(4, 4, 4, 4);
            }else{
                imageView = (ImageView)convertView;
            }
            imageView.setImageBitmap(bitmapArrayList.get(position));
            return imageView;
        }

        public ImageAdapter(Context context){
            this.context = context;
        }
    }
}
