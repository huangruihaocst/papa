package com.Fragments.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExperimentInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExperimentInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperimentInformationFragment extends Fragment {
    private static final String TAG = "ExperimentInformationFragment";
    private static final String ARG_BUNDLE_HELPER = "bundleHelper";

    private BundleHelper bundleHelper;
    View rootView;
    TextView textView_course_name;
    TextView textView_course_start_time;
    TextView textView_course_end_time;
    TextView textView_place;
    TextView textView_download;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExperimentInformationFragment.
     */
    public static ExperimentInformationFragment newInstance(BundleHelper bundleHelper) {
        ExperimentInformationFragment fragment = new ExperimentInformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BUNDLE_HELPER, bundleHelper);
        fragment.setArguments(args);
        return fragment;
    }

    public ExperimentInformationFragment() {
        // Required empty public constructor
    }

    private PapaDataBaseManager papaDataBaseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleHelper = getArguments().getParcelable(ARG_BUNDLE_HELPER);
            papaDataBaseManager = bundleHelper.getPapaDataBaseManager();
            get();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_experiment_information, container, false);
        ScrollView scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
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

    // get data from db
    void get() {
//        Log.i(TAG, bundleHelper.getCourseId() + " " + bundleHelper.getToken());
        new Task(getContext()).execute(new PapaDataBaseManager.GetLessonInfoRequest(
                bundleHelper.getExperimentId(),
                bundleHelper.getToken())
        );
    }

    // A-Z 想像High-de-Siehst YOU das?
    void set(PapaDataBaseManager.GetLessonInfoReply rlt) {
        textView_course_name = (TextView)rootView.findViewById(R.id.sender);
        textView_course_start_time = (TextView)rootView.findViewById(R.id.recipient);
        textView_course_end_time = (TextView)rootView.findViewById(R.id.course_end_time);
        textView_place = (TextView)rootView.findViewById(R.id.place);
        textView_download = (TextView)rootView.findViewById(R.id.download);

        textView_course_name.setText(rlt.name);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        simpleDateFormat.setTimeZone(rlt.startTime.getTimeZone());
        String time = (simpleDateFormat.format(rlt.startTime.getTime()));
        textView_course_start_time.setText(time);
        time = (simpleDateFormat.format(rlt.endTime.getTime()));
        textView_course_end_time.setText(time);
        textView_place.setText(rlt.location);
        textView_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_download.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("")
                //TODO:click here to download
            }
        });
    }

    // ☆愛-same-CRIER　愛撫-commit-LIAR
    // Eid-聖-Rising HELL
    // 愛してる Game世界のDay
    // Don't-生-War Lie-兵士-War-World
    // Eyes-Hate-War
    // A-Z Looser-Krankheit-Was IS das?

    class Task extends
            AsyncTask<PapaDataBaseManager.GetLessonInfoRequest, Exception, PapaDataBaseManager.GetLessonInfoReply> {
        ProgressDialog proDialog;

        public Task(Context context) {
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
        protected PapaDataBaseManager.GetLessonInfoReply doInBackground
                (PapaDataBaseManager.GetLessonInfoRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getLessonInfo(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.GetLessonInfoReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) set(rlt);
        }
    }

}
