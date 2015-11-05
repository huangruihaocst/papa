package com.Activities.papa.message;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
    SwipeRefreshLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_message, container, false);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
            }
        });
        layout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        messageListView = (ListView) layout.findViewById(R.id.listViewMessageList);

        // set the empty adapter and update it later
        adapter = new MessageListAdapter(getContext(), new MessageList());
        messageListView.setAdapter(adapter);

        return layout;
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
