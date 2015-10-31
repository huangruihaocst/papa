package com.Activities.papa.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alexwang on 10/31/15.
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

    /**
     * Always treat de-serialization as a full-blown constructor, by
     * validating the final state of the de-serialized object.
     */
//    private void readObject(ObjectInputStream aInputStream)
//            throws ClassNotFoundException, IOException {
//        //always perform the default de-serialization first
//        aInputStream.defaultReadObject();
//
//        // serialize other fields
//        int size = aInputStream.readInt();
//        for (int i = 0; i < size; ++i) {
//            messages.add((Message) aInputStream.readObject());
//        }
//    }
//
//    /**
//     * This is the default implementation of writeObject.
//     * Customise if necessary.
//     */
//    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
//        //perform the default serialization for all non-transient, non-static fields
//        aOutputStream.defaultWriteObject();
//        aOutputStream.writeInt(messages.size());
//        for (int i = 0; i < messages.size(); ++i) {
//            aOutputStream.writeObject(messages.get(i));
//        }
//    }
}
