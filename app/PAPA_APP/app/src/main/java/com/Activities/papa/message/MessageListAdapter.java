package com.Activities.papa.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
    MessagePullService service;
    MessageList validMessages;
    MessageList messages;
    boolean[] messageCheck;
    boolean editMode = false;

    public MessageListAdapter(Context context, MessageList messages) {
        this.context = context;
        resetData(messages);
    }

    public void resetData(MessageList messages) {
        this.messages = messages;
        this.validMessages = null;
        this.messageCheck = new boolean[getValidMessages().size()];
        notifyDataSetChanged();
    }

    public void enterEditMode() {
        editMode = true;
        notifyDataSetChanged();
    }
    public void quitEditMode(boolean delete) {
        editMode = false;
        if (delete) {
            for (int i = 0; i < getCount(); ++i) {
                if (messageCheck[i]) {
                    validMessages.get(i).ignoreMessage();
                }
            }
            service.syncFromApp(messages);
            validMessages = null;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getValidMessages().size();
    }

    @Override
    public Object getItem(int position) {
        return getValidMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.message_list_item, null);

        // set checkbox status
        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.check_box_message_delete_item);
        if (editMode)
            checkBox.setVisibility(View.VISIBLE);
        else
            checkBox.setVisibility(View.INVISIBLE);
        checkBox.setChecked(messageCheck[position]);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageCheck[position] = checkBox.isChecked();
            }
        });

        // title
        TextView title = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValidMessages().get(position).readMessage();
                if (service != null)
                    service.syncFromApp(messages);

                Intent intent = new Intent(context, MessageDetailActivity.class);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(getValidMessages().get(position));
                    oos.close();
                    byte[] bytes = bos.toByteArray();
                    bos.close();
                    intent.putExtra(context.getString(R.string.key_message_detail_activity_message_byte_array), bytes);
                    context.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Invalid Object Output Stream", Toast.LENGTH_SHORT).show();
                }
            }
        });
        title.setText(getValidMessages().get(position).getTitle());
        if (getValidMessages().get(position).getRead()) {
            title.setTextColor(Color.GREEN);
        }

        // content
        TextView content = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_content);
        content.setText(getValidMessages().get(position).getContent());

        return layout;
    }

    public void setMessageService(MessagePullService s) {
        this.service = s;
    }

    private MessageList getValidMessages() {
        if (validMessages == null) {
            validMessages = new MessageList();
            for (int i = 0; i < messages.size(); ++i) {
                if (!messages.get(i).getIgnored()) {
                    validMessages.add(messages.get(i));
                }
            }
        }
        return validMessages;
    }

}
