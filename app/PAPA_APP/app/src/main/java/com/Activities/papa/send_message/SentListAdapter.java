package com.Activities.papa.send_message;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
        TextView mTextView = new TextView(context);
        PapaDataBaseManager.ChatMessage chatMessage = lst.get(position);
        mTextView.setText(chatMessage.senderName);
        mTextView.setTextSize(25);
        mTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        return mTextView;
    }
}
