package com.Activities.papa.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a list of Message object only intended for serialization.
 */
public class MessageList implements Serializable, Iterable<Message> {
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
    public void addFront(Message msg) {
        messages.add(0, msg);
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
    public ArrayList<String> filterByMessageId(List<String> ids) {
        ArrayList<String> newIds = new ArrayList<>();
        for (int i = 0; i < ids.size(); ++i) {
            if (findById(ids.get(i)) == null) {
                newIds.add(ids.get(i));
            }
        }
        return newIds;
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }
}
