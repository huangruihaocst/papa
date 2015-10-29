package com.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.ExperimentActivity;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

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
    private static final String tag = "CourseFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final String ARG_PARAM2 = "ARG_PARAM2";
    private static final String ARG_PARAM3 = "ARG_PARAM3";

    // TODO: Rename and change types of parameters
    private int semester_id;
    private String token;
    private PapaDataBaseManager papaDataBaseManager;
    BundleHelper bundleHelper;

    View rootView;


//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(int id, String token, BundleHelper bundleHelper) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, token);
        args.putParcelable(ARG_PARAM3, bundleHelper);
        fragment.setArguments(args);
        return fragment;
    }

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            semester_id = getArguments().getInt(ARG_PARAM1);
            token = getArguments().getString(ARG_PARAM2);
            bundleHelper = getArguments().getParcelable(ARG_PARAM3);
        }

        papaDataBaseManager = BundleHelper.getPapaDataBaseManager();
    }


    ListView course_student_list;
    ListView course_teacher_assistant_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView == null){
            rootView  = inflater.inflate(R.layout.fragment_course, container, false);
            course_teacher_assistant_list =
                    (ListView)rootView.findViewById(R.id.course_teacher_assistant_list);
            course_student_list = (ListView)rootView.findViewById(R.id.course_student_list);
            getStudentCourses();
            getTeacherCourses();
        }else{
            ((ViewGroup)rootView.getParent()).removeView(rootView);
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



    // Courses
    private void getStudentCourses() {
        new GetStudentCourseTask(getContext()).execute(new PapaDataBaseManager.CourseRequest(semester_id, token));
    }

    private void getTeacherCourses() {
        new GetTeacherCourseTask(getContext()).execute(new PapaDataBaseManager.CourseRequest(semester_id, token));
    }

    class GetStudentCourseTask extends
            AsyncTask<PapaDataBaseManager.CourseRequest, Exception, PapaDataBaseManager.CourseReply> {
        ProgressDialog proDialog;

        public GetStudentCourseTask(Context context) {
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
        protected PapaDataBaseManager.CourseReply doInBackground
                (PapaDataBaseManager.CourseRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getStuCourse(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.CourseReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setStudentCourses(rlt);
        }
    }


    private class MyAdapter extends BaseAdapter {
        private List<Map.Entry<Integer, String>> lst;

        public MyAdapter(List<Map.Entry<Integer, String>> lst) {
            this.lst = lst;
        }

        @Override
        public int getCount() {
            return lst.size();
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
            TextView mTextView = new TextView(getContext());
            mTextView.setText(lst.get(position).getValue());
            mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
            mTextView.setTextColor(Color.parseColor(getString(R.string.color_primary)));
            return mTextView;
        }
    }



    private void startExperimentActivity(String courseName, int courseId, String identity){

        Log.i(tag, courseName + "=" + courseId);

        Intent intent = new Intent(getActivity(), ExperimentActivity.class);
        Bundle data = new Bundle();
        String key_course_experiment = getString(R.string.key_course_experiment);
        bundleHelper.setCourseName(courseName);
        bundleHelper.setCourseId(courseId);
        bundleHelper.setIdentity(identity);
        if(identity.equals("student")) {
            bundleHelper.setStudentId(bundleHelper.getId());
            bundleHelper.setStudentName(bundleHelper.getUsername());
        }
        data.putParcelable(key_course_experiment,bundleHelper);
        intent.putExtras(data);
        startActivity(intent);
    }




    class GetTeacherCourseTask extends
            AsyncTask<PapaDataBaseManager.CourseRequest, Exception, PapaDataBaseManager.CourseReply> {
        ProgressDialog proDialog;

        public GetTeacherCourseTask(Context context) {
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
        protected PapaDataBaseManager.CourseReply doInBackground
                (PapaDataBaseManager.CourseRequest... params) {
            // 在后台
            try {
                return papaDataBaseManager.getTACourse(params[0]);
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
        protected void onPostExecute(PapaDataBaseManager.CourseReply rlt) {
            // UI

            proDialog.dismiss();
            if (rlt != null) setTeacherCourses(rlt);
        }
    }

    private void setStudentCourses(final PapaDataBaseManager.CourseReply rlt) {
        ListView CourseStudentListView = course_student_list;
        CourseStudentListView.setAdapter(new MyAdapter(rlt.course));
        CourseStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startExperimentActivity(rlt.course.get(position).getValue(), rlt.course.get(position).getKey(), "student");
            }
        });
    }


    private void setTeacherCourses(final PapaDataBaseManager.CourseReply rlt) {
        ListView CourseTeacherAssistantListView = course_teacher_assistant_list;
        CourseTeacherAssistantListView.setAdapter(new MyAdapter(rlt.course));
        CourseTeacherAssistantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startExperimentActivity(rlt.course.get(position).getValue(), rlt.course.get(position).getKey(), "teacher_assistant");
            }
        });
    }
}
