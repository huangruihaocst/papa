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

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {
    ListView messageListView;
    MessageListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_message, container, false);
        messageListView = (ListView) fragment.findViewById(R.id.listViewMessageList);

        // TODO
        // get message list
        // let the user wait
//        ArrayList<Message> messages = new ArrayList<>();
//        for (int i = 0  ; i < 20; ++i) {
//            messages.add(new Message(String.valueOf(i), "Title " + String.valueOf(i), "notification", "Content"));
//        }
//        messageListView.setAdapter(new MessageListAdapter(getContext(), messages));

        adapter = new MessageListAdapter(getContext(), new MessageList());
        messageListView.setAdapter(adapter);

        return fragment;
    }

    public void updateMessages(MessageList list) {
        adapter.resetData(list);
        //messageListView.setAdapter(new MessageListAdapter(getContext(), list));

        Toast.makeText(getContext(), "update messages", Toast.LENGTH_SHORT).show();
    }
}
