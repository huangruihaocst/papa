package com.Fragments.papa;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GradesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GradesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradesFragment extends Fragment {
    private static final String TAG = "GradesFragment";
    private static final String ARG_BUNDLE_HELPER = "bundleHelper";

    private BundleHelper bundleHelper;

    private OnFragmentInteractionListener mListener;

    TextView user_id;
    TextView user_class;
    TextView user_grades;
    TextView user_comment;
    TextView user_evaluator;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GradesFragment.
     */
    public static GradesFragment newInstance(BundleHelper bundleHelper) {
        GradesFragment fragment = new GradesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BUNDLE_HELPER, bundleHelper);
        fragment.setArguments(args);
        return fragment;
    }

    public GradesFragment() {
        // Required empty public constructor
    }

    private PapaDataBaseManager papaDataBaseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleHelper = getArguments().getParcelable(ARG_BUNDLE_HELPER);
            papaDataBaseManager = bundleHelper.getPapaDataBaseManager();
            getComment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_grades, container, false);
        user_id = (TextView)rootView.findViewById(R.id.sender);
        user_class = (TextView)rootView.findViewById(R.id.recipient);
        user_grades = (TextView)rootView.findViewById(R.id.sent_time);
        user_comment = (TextView)rootView.findViewById(R.id.title);
        user_evaluator = (TextView)rootView.findViewById(R.id.body);
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


    //call this in another thread
    private void getComment(){
        new GetCommentTask(getContext()).execute(
            new PapaDataBaseManager.GetCommentsRequest(
                    bundleHelper.getExperimentId(),
                    bundleHelper.getStudentId(),
                    bundleHelper.getToken()
            )
        );
    }

    private void setGrades(PapaDataBaseManager.GetCommentsReply reply) {
        user_id.setText(reply.stuId);
        user_class.setText(reply.className);
        user_grades.setText(reply.score);
        user_comment.setText(reply.comments);
        user_evaluator.setText(reply.creatorName);
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
            // if(e)
            // UI
            Toast.makeText(getContext(), e[0].getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(PapaDataBaseManager.GetCommentsReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setGrades(rlt);
        }
    }
}
