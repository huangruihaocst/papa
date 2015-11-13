package com.Fragments.papa.course;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Activities.papa.experiments.ExperimentActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 15-11-13.
 */
public class CourseListAdapter extends BaseAdapter {
    private List<Map.Entry<Integer, String>> studentCourses;
    private List<Map.Entry<Integer, String>> taCourses;
    Context context;
    BundleHelper bundleHelper;

    public CourseListAdapter(List<Map.Entry<Integer, String>> studentCourses, List<Map.Entry<Integer, String>> taCourses, Context context, BundleHelper bundleHelper) {
        this.studentCourses = studentCourses;
        this.taCourses = taCourses;
        this.context = context;
        this.bundleHelper = bundleHelper;
    }

    // 2 means table name
    @Override
    public int getCount() {
        return studentCourses.size() + taCourses.size() + 2;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView != null) {
            textView = (TextView) convertView;
        } else {
            textView = new TextView(context);
        }

        if (position == 0) {
            textView.setText(context.getString(R.string.student_course));
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            textView.setTextSize(40);
        } else if (position <= studentCourses.size()) {
            final int index = position - 1;
            textView.setText(studentCourses.get(index).getValue());
            textView.setTextSize(25);
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startExperimentActivity(
                            context,
                            studentCourses.get(index).getValue(),
                            studentCourses.get(index).getKey(),
                            bundleHelper,
                            BundleHelper.Identity.student);
                }
            });
        } else if (position == studentCourses.size() + 1) {
            textView.setText(context.getString(R.string.teacher_assistant_course));
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            textView.setTextSize(40);
        } else {
            final int index = position - 2 - studentCourses.size();
            textView.setText(taCourses.get(index).getValue());
            textView.setTextSize(25);
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startExperimentActivity(
                            context,
                            taCourses.get(index).getValue(),
                            taCourses.get(index).getKey(),
                            bundleHelper,
                            BundleHelper.Identity.teacher_assistant);
                }
            });
        }
        
        return textView;
    }

    private void startExperimentActivity(Context context,
                                         String courseName,
                                         int courseId,
                                         BundleHelper bundleHelper,
                                         BundleHelper.Identity identity){
        Intent intent = new Intent(context, ExperimentActivity.class);
        Bundle data = new Bundle();
        String key_course_experiment = context.getString(R.string.key_course_experiment);
        bundleHelper.setCourseName(courseName);
        bundleHelper.setCourseId(courseId);
        bundleHelper.setIdentity(identity);
        if(identity == BundleHelper.Identity.student) {
            bundleHelper.setStudentId(bundleHelper.getId());
            bundleHelper.setStudentName(bundleHelper.getUsername());
        }
        data.putParcelable(key_course_experiment,bundleHelper);
        intent.putExtras(data);
        context.startActivity(intent);
    }
}