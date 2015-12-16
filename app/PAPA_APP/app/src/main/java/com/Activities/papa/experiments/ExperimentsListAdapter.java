package com.Activities.papa.experiments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.R;

import java.util.List;
import java.util.Map;

/**
 * Created by huang on 15-11-12.
 */
public class ExperimentsListAdapter extends BaseAdapter {
    private List<Map.Entry<Integer, String>> lst;
    Context context;

    public ExperimentsListAdapter(List<Map.Entry<Integer, String>> lst, Context context) {
        this.lst = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.experiment_list_item, null);
        }
        TextView experiment_name = (TextView)convertView.findViewById(R.id.experiment_name);
        TextView information = (TextView)convertView.findViewById(R.id.information);

        experiment_name.setText(lst.get(position).getValue());
        String experiment_info = "地点：北京大学 时间:2015-1-1 00:00:00至2015-12-31 23:59:59";//faked

        int length = experiment_info.length();
        int max_length = context.getResources().getInteger(R.integer.overview_max_length);
        if(length > max_length){
            experiment_info = experiment_info.substring(0, max_length + 1);
            experiment_info += "...";
        }
        information.setText(experiment_info);

        return convertView;
    }
}
