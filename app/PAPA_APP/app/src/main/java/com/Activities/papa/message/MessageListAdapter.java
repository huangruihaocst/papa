package com.Activities.papa.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Activities.papa.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MessageListAdapter extends BaseAdapter {
    Context context;
    private MessageList messages;

    public MessageListAdapter(Context context, MessageList messages) {
        this.context = context;
        this.messages = messages;
    }

    public void resetData(MessageList messages) {
        this.messages = messages;
        //notifyDataSetInvalidated();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
        TextView title = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(messages.get(position));
                    oos.close();
                    byte[] bytes = bos.toByteArray();
                    bos.close();
                    intent.putExtra(context.getString(R.string.key_message_detail_activity_message_byte_array), bytes);
                    context.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Invalid OOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView content = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_content);
        title.setText(messages.get(position).getTitle());
        content.setText(messages.get(position).getContent());
        return layout;
    }
}
