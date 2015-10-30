package com.Fragments.papa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.DetailActivity;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.Map;

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
    private static final String ARG_BUNDLEHELPER = "bundleHelper";

    private BundleHelper bundleHelper;

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
        args.putParcelable(ARG_BUNDLEHELPER, bundleHelper);
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
            bundleHelper = getArguments().getParcelable(ARG_BUNDLEHELPER);
            papaDataBaseManager = bundleHelper.getPapaDataBaseManager();
            get();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiment_information, container, false);
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
        Log.i(TAG, bundleHelper.getCourseId() + " " + bundleHelper.getToken());
        new Task(getContext()).execute(new PapaDataBaseManager.GetLessonInfoRequest(
                bundleHelper.getExperimentId(),
                bundleHelper.getToken())
        );
    }

    // A-Z 想像High-de-Siehst YOU das?
    void set(PapaDataBaseManager.GetLessonInfoReply rlt)
    {
        final TextView textView = (TextView)getView().findViewById(R.id.experiment_info);

        String str = "name = " + rlt.name +
                "\n" +
                "start /*dash*/ = " + rlt.startTime +
                "\n" +
                "end = " + rlt.endTime + /*" (LaoBi: Every good thing has an end. Time's up.)" +*/
                "\n" +
                "where = " + rlt.location;

        textView.setText(str);
    }

    // ☆愛-same-CRIER　愛撫-save-LIAR
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
