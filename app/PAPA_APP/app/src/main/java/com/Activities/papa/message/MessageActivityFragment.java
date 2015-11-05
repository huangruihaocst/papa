package com.Activities.papa.message;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.Activities.papa.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {
    ListView messageListView;
    MessageListAdapter adapter;
    MessagePullService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_message, container, false);
        messageListView = (ListView) fragment.findViewById(R.id.listViewMessageList);

        // set the empty adapter and update it later
        adapter = new MessageListAdapter(getContext(), new MessageList());
        messageListView.setAdapter(adapter);

        return fragment;
    }

    public void updateMessages(MessageList list) {
        adapter.resetData(list);
    }

    public void setMessageService(MessagePullService s) {
        this.service = s;
        if (this.adapter != null)
            adapter.setMessageService(s);
    }

    public void enterEditMode() {
        adapter.enterEditMode();
    }

    public void quitEditMode(boolean delete) {
        adapter.quitEditMode(delete);
    }
    public void markAllAsRead() {
        adapter.markAllAsRead();
    }
}
