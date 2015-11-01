package com.Activities.papa.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a list of Message object only intended for serialization.
 */
public class MessageList implements Serializable {
    ArrayList<Message> messages = new ArrayList<>();

    public MessageList() {
    }

    public MessageList(ArrayList<Message> messageArrayList) {
        this.messages = messageArrayList;
    }
    public void add(Message msg) {
        messages.add(msg);
    }
    public void add(int pos, Message msg) {
        messages.add(pos, msg);
    }
    public Message get(int position) {
        return messages.get(position);
    }
    public int size() {
        return messages.size();
    }
    public void remove(int pos) {
        messages.remove(pos);
    }
    public ArrayList<Message> toArrayList() {
        return messages;
    }
    public Message[] toArray() {
        return messages.toArray(new Message[messages.size()]);
    }
    public Message findById(String id) {
        for (int i = 0; i < messages.size(); ++i) {
            if (messages.get(i).getId().equals(id)) {
                return messages.get(i);
            }
        }
        return null;
    }
    public ArrayList<String> filterByMessageId(ArrayList<String> ids) {
        ArrayList<String> newIds = new ArrayList<>();
        for (int i = 0; i < ids.size(); ++i) {
            if (findById(ids.get(i)) == null) {
                newIds.add(ids.get(i));
            }
        }
        return newIds;
    }

}
