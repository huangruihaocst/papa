package com.Fragments.papa.student;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.R;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 15-11-13.
 */
public class StudentsListAdapter extends BaseAdapter {
    private List<Map.Entry<Integer, PapaDataBaseManager.StudentInfo>> lst;
    Context context;

    public StudentsListAdapter(
            List<Map.Entry<Integer, PapaDataBaseManager.StudentInfo>> lst, Context context) {
        this.lst = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Map.Entry<Integer, PapaDataBaseManager.StudentInfo> getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.students_list_item, null);
        }

        TextView student_name = (TextView)convertView.findViewById(R.id.student_name);
        TextView student_id = (TextView)convertView.findViewById(R.id.student_id);
        PapaDataBaseManager.StudentInfo studentInfo = lst.get(position).getValue();
        student_name.setText(studentInfo.name);
        student_id.setText("学号：" + studentInfo.studentNumber);
        return convertView;
    }
}
