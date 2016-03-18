package com.Fragments.papa.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Helpers.BundleHelper;
import com.Activities.papa.R;
import com.Activities.papa.experiments.ExperimentActivity;

import java.util.List;
import java.util.Map;

/**
 * CourseRecyclerAdapter
 * Convert studentCourses and taCourses into list elements in RecyclerView
 */
public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {
    private List<Map.Entry<Integer, String>> studentCourses;
    private List<Map.Entry<Integer, String>> taCourses;
    Context context;
    BundleHelper bundleHelper;

    public CourseRecyclerAdapter(List<Map.Entry<Integer, String>> studentCourses,
                                 List<Map.Entry<Integer, String>> taCourses,
                                 Context context,
                                 BundleHelper bundleHelper) {
        this.studentCourses = studentCourses;
        this.taCourses = taCourses;
        this.context = context;
        this.bundleHelper = bundleHelper;
    }

    @Override
    public int getItemCount() {
        // the magic number 2 is for title 'student course' and 'ta course'
        if(studentCourses.size() == 0 && taCourses.size() == 0){
            return 0;
        }else if(studentCourses.size() == 0){
            return taCourses.size() + 1;
        }else if(taCourses.size() == 0){
            return  studentCourses.size() + 1;
        }else{
            return studentCourses.size() + taCourses.size() + 2;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.course_list_item, parent, false);
        ((RecyclerView)parent).addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Toast.makeText(context, "Touch", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View itemView = holder.getLayout();
        TextView textView = holder.getTitleView();

        if (studentCourses.size() == 0 && taCourses.size() == 0) {
            return;
        }
        else if (taCourses.size() == 0) {
            if (position == 0) {
                textView.setText(context.getString(R.string.student_course));
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                textView.setTextSize(context.getResources().getInteger(R.integer.course_title_text_size));
            } else if (position < studentCourses.size() + 1) {
                final int index = position - 1;
                textView.setText(studentCourses.get(index).getValue());
                textView.setTextSize(context.getResources().getInteger(R.integer.course_text_size));
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
            }
            else
                return;
        }
        else if (studentCourses.size() == 0) {
            if (position == 0) {
                textView.setText(context.getString(R.string.teacher_assistant_course));
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                textView.setTextSize(context.getResources().getInteger(R.integer.course_title_text_size));
            } else if (position < taCourses.size() + 1) {
                final int index = position - 1;
                textView.setText(taCourses.get(index).getValue());
                textView.setTextSize(context.getResources().getInteger(R.integer.course_text_size));
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
            else
                return;
        }
        else {
            if (position == 0) {
                textView.setText(context.getString(R.string.student_course));
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                textView.setTextSize(context.getResources().getInteger(R.integer.course_title_text_size));
            } else if (position < studentCourses.size() + 1) {
                final int index = position - 1;
                textView.setText(studentCourses.get(index).getValue());
                textView.setTextSize(context.getResources().getInteger(R.integer.course_text_size));
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
                textView.setTextSize(context.getResources().getInteger(R.integer.course_title_text_size));
            } else {
                final int index = position - 2 - studentCourses.size();
                textView.setText(taCourses.get(index).getValue());
                textView.setTextSize(context.getResources().getInteger(R.integer.course_text_size));
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                itemView.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    private void startExperimentActivity(Context context, String courseName, int courseId,
                                         BundleHelper bundleHelper, BundleHelper.Identity identity){
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.course_list_item_text);
        }

        public View getLayout() {
            return itemView;
        }
        public TextView getTitleView() {
            return tvTitle;
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
