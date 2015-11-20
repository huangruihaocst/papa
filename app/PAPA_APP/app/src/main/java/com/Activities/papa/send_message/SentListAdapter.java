package com.Activities.papa.send_message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Activities.papa.R;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by huang on 15-11-13.
 */
public class SentListAdapter extends BaseAdapter {
    private List<PapaDataBaseManager.ChatMessage> lst;
    Context context;

    public SentListAdapter(List<PapaDataBaseManager.ChatMessage> lst, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sent_list_item, null);
        }
        TextView send_name = (TextView)convertView.findViewById(R.id.send_name);
        TextView send_overview = (TextView)convertView.findViewById(R.id.send_overview);
        TextView send_time = (TextView)convertView.findViewById(R.id.send_time);
        PapaDataBaseManager.ChatMessage chatMessage = lst.get(position);
        send_name.setText(chatMessage.senderName);
        int max_length = context.getResources().getInteger(R.integer.send_overview_max_length);
        String overview = chatMessage.title + ":" + chatMessage.content;
        if(overview.length() > max_length){
            overview = overview.substring(0, max_length - 3) + "...";
        }
        send_overview.setText(overview);
        Calendar send_time_calendar = chatMessage.created_at;
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(String.format(context.getString(R.string.time_format_simple), "MM", "dd") + " HH:mm",
                        Locale.CHINA);
        simpleDateFormat.setTimeZone(send_time_calendar.getTimeZone());
        String time = (simpleDateFormat.format(send_time_calendar.getTime()));
        send_time.setText(time);
        return convertView;
    }
}
