package com.Fragments.papa.course;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.R;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 15-11-13.
 */
public class CourseListAdapter extends BaseAdapter {
    private List<Map.Entry<Integer, String>> lst;
    Context context;

    public CourseListAdapter(List<Map.Entry<Integer, String>> lst, Context context) {
        this.lst = lst;
        this.context = context;
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
        TextView mTextView = new TextView(context);
        mTextView.setText(lst.get(position).getValue());
        mTextView.setTextSize(35);
//            mTextView.setTextColor(getColor(R.color.colorPrimary));
        mTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        return mTextView;
    }
}