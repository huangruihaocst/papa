package com.Activities.papa.message;

import android.content.Context;
import android.content.Intent;
//import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;

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

    /**
     * This function should be called every time the messages are updated.
     *  Or users cannot be informed of the changes.
     * @param messages new messages to set.
     */
    public void resetData(MessageList messages) {
        this.messages = sortMessages(messages);
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

    /**
     * Click a single message.
     */
    class MessageClickListener implements View.OnClickListener {
        int position;
        public MessageClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            final Message message = getValidMessages().get(position);
            message.readMessage();
            if (service != null)
                service.syncFromApp(messages);

            Intent intent = new Intent(context, MessageDetailActivity.class);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(message);
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
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        ViewGroup layout;

        /**
         * Reuse convertView if created before.
         */
        if (convertView == null) {
            layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
            layout.setOnClickListener(new MessageClickListener(position));
        }
        else {
            layout = (ViewGroup) convertView;
        }

        /**
         * Edit mode status.
         */
        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.check_box_message_delete_item);
        if (!editMode) {
            checkBox.setVisibility(View.INVISIBLE);
        }
        else {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(messageCheck[position]);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageCheck[position] = checkBox.isChecked();
                }
            });
        }

        final Message message = getValidMessages().get(position);
        TextView title = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_title);
        title.setText(message.getTitle());

        /**
         * Set title color.
         *      Outdated: light gray
         *      Nearing deadline: read
         *      Read: dark gray
         *      Normal: black
         */
        HashMap<Message.Status, Integer> mapStatusColor = new HashMap<>();
        mapStatusColor.put(Message.Status.Critical, ContextCompat.getColor(context, R.color.color_message_nearing_deadline));
        mapStatusColor.put(Message.Status.Normal, ContextCompat.getColor(context, R.color.color_message_normal));
        mapStatusColor.put(Message.Status.Read, ContextCompat.getColor(context, R.color.color_message_read));
        mapStatusColor.put(Message.Status.Outdated, ContextCompat.getColor(context, R.color.color_message_after_deadline));
        // just in case
        mapStatusColor.put(Message.Status.Ignored, ContextCompat.getColor(context, R.color.color_message_after_deadline));

        title.setTextColor(mapStatusColor.get(message.getStatus()));

        // content
        TextView content = (TextView)layout.findViewById(R.id.text_view_message_list_view_item_content);
        content.setText(message.getTruncatedContent(context.getResources().getInteger(R.integer.max_displayed_content_length)));

        return layout;
    }

    public void setMessageService(MessagePullService s) {
        this.service = s;
    }

    /**
     * Get all the messages that are not ignored.
     * @return result.
     */
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

    /**
     * Sort all the messages as the following order:
     *      1. Normal
     *      2. Read
     *      3. Outdated
     *      4. Ignore
     *  and in each part the messages are sorted reverse by deadline.
     * @param messages the messages to be sort.
     * @return the sorted messages
     */
    private static MessageList sortMessages(MessageList messages) {
        ArrayList<Message> ignored = new ArrayList<>(),
                outdated = new ArrayList<>(),
                read = new ArrayList<>(),
                normal = new ArrayList<>(),
                critical = new ArrayList<>();

        for (Message message : messages) {
            switch (message.getStatus()) {
                case Critical:
                    critical.add(message);
                    break;
                case Normal:
                    normal.add(message);
                    break;
                case Ignored:
                    ignored.add(message);
                    break;
                case Read:
                    read.add(message);
                    break;
                case Outdated:
                    outdated.add(message);
                    break;
            }
        }

        Comparator<Message> comparator = new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return -(int)(lhs.getDeadline().getTimeInMillis() -
                        rhs.getDeadline().getTimeInMillis());
            }
        };
        Collections.sort(critical, comparator);
        Collections.sort(normal, comparator);
        Collections.sort(ignored, comparator);
        Collections.sort(read, comparator);
        Collections.sort(outdated, comparator);

        ArrayList<Message> newMessages = new ArrayList<>();
        newMessages.addAll(critical);
        newMessages.addAll(normal);
        newMessages.addAll(read);
        newMessages.addAll(outdated);
        newMessages.addAll(ignored);
        return new MessageList(newMessages);
    }

    void markAllAsRead() {
        for (Message message : messages) {
            message.readMessage();
        }
        resetData(messages);
    }
}
