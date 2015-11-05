package com.Fragments.papa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.Activities.papa.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExperimentResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExperimentResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Code for our image picker select action.
    private static final int IMAGE_PICKER_SELECT = 999;
    private static final int CAPTURE_IMAGE = 998;
    private static final int CAPTURE_VIDEO = 997;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri;

    // Reference to our image view we will use
//    private ImageView selectedImage;
//    private VideoView selectedVideo;
    GridView gridView_image;
    byte[] bytes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExperimentResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperimentResultFragment newInstance(String param1, String param2) {
        ExperimentResultFragment fragment = new ExperimentResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        gridView_image.setAdapter(new ImageAdapter(getContext()));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.select_type)).setItems(R.array.upload_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){//camera
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                            // start the image capture Intent
                            startActivityForResult(intent, CAPTURE_IMAGE);
                        }else if(which == 1){//video
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                            // start the Video Capture Intent
                            startActivityForResult(intent, CAPTURE_VIDEO);
                        }else if(which == 2){//gallery
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*,video/*");
                            startActivityForResult(intent, IMAGE_PICKER_SELECT);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    selectedImage.setImageBitmap(bitmap);
                    byte[] bytes = toByteArray(file);
                }
            }
        }else if(requestCode == CAPTURE_IMAGE){
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(getContext(), "Image saved to:\n" +
                        fileUri.toString(), Toast.LENGTH_LONG).show();
                String path = fileUri.getPath();
                if (path != null) {
                    File file = new File(path);
                    if(file.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        selectedImage.setImageBitmap(bitmap);
                        byte[] bytes = toByteArray(file);
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
                Toast.makeText(getContext(), "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                String path = fileUri.getPath();
                if (path != null) {
                    File file = new File(path);
                    if(file.exists()){
//                        selectedVideo.setVideoPath(path);
//                        selectedVideo.start();
                        byte[] bytes = toByteArray(file);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
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

    public byte[] toByteArray(File file){
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    public class ImageAdapter extends BaseAdapter{

        private Context context;

        @Override
        public int getCount() {
            return ImageId.length;
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
            }else{
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(ImageId[position]);
            Toast.makeText(getContext(),ImageId[position],Toast.LENGTH_LONG).show();
            return imageView;
        }

        public ImageAdapter(Context context){
            Toast.makeText(getContext(),"created",Toast.LENGTH_LONG).show();
            this.context = context;
        }

        private int[] ImageId = {
                R.drawable.ic_file_upload_black_24dp,
                R.drawable.ic_history_black_24dp,
                R.drawable.ic_info_black_24dp,
                R.drawable.ic_notifications_black_24dp
        };
    }
}
