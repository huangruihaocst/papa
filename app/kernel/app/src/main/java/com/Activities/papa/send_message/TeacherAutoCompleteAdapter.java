package com.Activities.papa.send_message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.util.ArrayList;

/**
 * Created by huang on 15-11-18.
 */
public class TeacherAutoCompleteAdapter extends ArrayAdapter<PapaDataBaseManager.TeacherInfo> {

    ArrayList<PapaDataBaseManager.TeacherInfo> items;
    Context context;
    int layout_resource_id;

    public TeacherAutoCompleteAdapter(Context context, int layout_resource_id,
                                      ArrayList<PapaDataBaseManager.TeacherInfo> items){
        super(context, layout_resource_id);
        this.items = items;
        this.context = context;
        this.layout_resource_id = layout_resource_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
         if(convertView == null){
             LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = layoutInflater.inflate(layout_resource_id, null);
         }
        PapaDataBaseManager.TeacherInfo teacherInfo = items.get(position);
        if(teacherInfo != null){
            TextView textView = new TextView(context);
            textView.setText(teacherInfo.toString());
        }
        return convertView;
    }


}
