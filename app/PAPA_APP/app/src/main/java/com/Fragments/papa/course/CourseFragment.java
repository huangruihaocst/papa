package com.Fragments.papa.course;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.Helpers.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Fragments.papa.DividerItemDecoration;

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "CourseFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final String ARG_PARAM2 = "ARG_PARAM2";
    private static final String ARG_PARAM3 = "ARG_PARAM3";
    private static final String ARG_PARAM4 = "ARG_PARAM4";

    // TODO: Rename and change types of parameters
    private int semesterId;
    private String token;
    private int id;
    private PapaDataBaseManager papaDataBaseManager;
    BundleHelper bundleHelper;

    View rootView;


//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(int id, int semesterId, String token, BundleHelper bundleHelper) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, token);
        args.putInt(ARG_PARAM3, semesterId);
        args.putParcelable(ARG_PARAM4, bundleHelper);
        fragment.setArguments(args);
        return fragment;
    }

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            token = getArguments().getString(ARG_PARAM2);
            semesterId = getArguments().getInt(ARG_PARAM3);
            bundleHelper = getArguments().getParcelable(ARG_PARAM4);
        }

        papaDataBaseManager = BundleHelper.getPapaDataBaseManager();
    }

    RecyclerView courseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_course, container, false);

            // Set RecyclerView style: Would this be better ?
            // +--------+
            // | ##  ## |
            // | ##  ## |
            // | ##  ## |
            // +--------+
            courseList = (RecyclerView) rootView.findViewById(R.id.course_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            courseList.setLayoutManager(layoutManager);
            courseList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

            getCourses();
        }
        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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


    private void getCourses() {
        new AsyncTask<Object[], Exception, Object[]>() {
            ProgressDialog proDialog;

            @Override
            protected void onPreExecute() {
                // UI
                proDialog = new ProgressDialog(getContext(), 0);
                proDialog.setMessage("稍等喵 =w=");
                proDialog.setCancelable(false);
                proDialog.setCanceledOnTouchOutside(false);
                proDialog.show();
            }

            @Override
            protected Object[] doInBackground
            (Object[] ...params) {
                // 在后台
                try {
                    PapaDataBaseManager.CourseReply taCourseReply =
                        papaDataBaseManager.getTACourse(new PapaDataBaseManager.CourseRequest(id, semesterId, token));
                    PapaDataBaseManager.CourseReply studentCourseReply =
                            papaDataBaseManager.getStuCourse(new PapaDataBaseManager.CourseRequest(id, semesterId, token));

                    return new Object[] {
                            studentCourseReply,
                            taCourseReply
                    };
                } catch (PapaHttpClientException e) {
                    publishProgress(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object[] rlt) {
                // UI
                proDialog.dismiss();
                if (rlt != null) {
                    PapaDataBaseManager.CourseReply studentCourseReply = (PapaDataBaseManager.CourseReply) rlt[0];
                    PapaDataBaseManager.CourseReply taCourseReply = (PapaDataBaseManager.CourseReply) rlt[1];
                    setCourses(studentCourseReply.course, taCourseReply.course);
                }
            }
        }.execute();
    }

    private void setCourses(List<Map.Entry<Integer, String>> studentCourses, List<Map.Entry<Integer, String>> taCourses) {
        courseList.setAdapter(new CourseRecyclerAdapter(studentCourses, taCourses, getContext(), bundleHelper));
//        setListViewHeightBasedOnChildren(courseList);
        courseList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
