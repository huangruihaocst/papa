package com.Activities.papa.experiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.R;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by huang on 15-11-12.
 */
public class ExperimentsListAdapter extends BaseAdapter {
    private List<Map.Entry<Integer, PapaDataBaseManager.LessonInfo>> lst;
    Context context;

    public ExperimentsListAdapter(List<Map.Entry<Integer, PapaDataBaseManager.LessonInfo>> lst,
                                  Context context) {
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

        PapaDataBaseManager.LessonInfo info = lst.get(position).getValue();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                context.getResources().getConfiguration().locale);
        simpleDateFormat.setTimeZone(info.startTime.getTimeZone());
        String start_time = (simpleDateFormat.format(info.startTime.getTime()));
        String end_time = (simpleDateFormat.format(info.endTime.getTime()));

        experiment_name.setText(info.name);
        String experiment_info = context.getString(R.string.location) + info.location + " " + context.getString(R.string.time) +
                start_time + context.getString(R.string.to) + end_time;

        int length = experiment_info.length();
        int max_length = context.getResources().getInteger(R.integer.experiment_overview_max_length);
        if(length > max_length - 3){
            experiment_info = experiment_info.substring(0, max_length - 3);
            experiment_info += "...";
        }
        information.setText(experiment_info);

        return convertView;
    }
}
