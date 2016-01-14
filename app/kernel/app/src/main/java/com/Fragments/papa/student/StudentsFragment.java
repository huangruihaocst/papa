package com.Fragments.papa.student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Helpers.BundleHelper;
import com.Activities.papa.DetailActivity;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.Map;

//
// @ 坐和放宽
// 请把接下来要继续的 Activity 连接好.
// 我个人觉得这些逻辑是属于 UI 的一部分
//
// 我上网帮你调取必要的数据, 但我对你的 Activity 的逻辑确实不太了解.
// 我怕给你连乱了.
//
// 我今天晚上帮你把 StudentFragment 自作主张地连接到我自认为的正确的地方. 今后请你继续加油
//
// 包括在 29 号以前的所有话都是我给你的参考 你可以不采纳, 也可以采纳, 取决你的想法.
//
// 翔翔

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentsFragment extends Fragment {
    private static final String TAG = "StudentsFragment";
    private static final String ARG_BUNDLE_HELPER = "bundleHelper";

    private BundleHelper bundleHelper;

    private OnFragmentInteractionListener mListener;

    View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StudentsFragment.
     */
    public static StudentsFragment newInstance(BundleHelper bundleHelper) {
        StudentsFragment fragment = new StudentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BUNDLE_HELPER, bundleHelper);
        fragment.setArguments(args);
        return fragment;
    }

    public StudentsFragment() {
        // Required empty public constructor
    }

    private PapaDataBaseManager papaDataBaseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleHelper = getArguments().getParcelable(ARG_BUNDLE_HELPER);
            papaDataBaseManager = bundleHelper.getPapaDataBaseManager();
            getStudents();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_students, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    //
    // Leben, was ist das?
    // Signal, Siehst du das?
    // Rade, die du nicht weisst
    // Aus eigenem Willen
    //
    // Leben, was ist das?
    // Signal, Siehst du das?
    // Rade, die du nicht weisst
    // Sieh mit deinen Augen
    //

    void getStudents() {
        Log.i(TAG, bundleHelper.getCourseId() + " " + bundleHelper.getToken());
        new GetStudentsTask(getContext()).execute(new PapaDataBaseManager.StudentsRequest(
                bundleHelper.getCourseId(),
                bundleHelper.getToken())
        );
    }

    void setStudents(PapaDataBaseManager.StudentsReply rlt)
    {
        final ListView StudentListView = (ListView)rootView.findViewById(R.id.student_list);
        StudentListView.setAdapter(new StudentsListAdapter(rlt.students, getContext()));
        StudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<Integer, String> item =
                        (Map.Entry<Integer, String>) parent.getItemAtPosition(position);

                Log.i(TAG, item.getValue() + " " + item.getKey());

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                String key_to_detail = getString(R.string.key_to_detail);
                Bundle data = new Bundle();
                bundleHelper.setStudentId(item.getKey());
                bundleHelper.setStudentName(item.getValue());
                data.putParcelable(key_to_detail, bundleHelper);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
        StudentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("shortcut to edit grades here");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
        StudentListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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

}
