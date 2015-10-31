package com.Activities.papa.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by alexwang on 10/31/15.
 */
public class Message implements Serializable {
    String id, title, type, content;
    boolean ignored = false;
    boolean read = false;
    boolean newMessage = true;

    public final static Message InvalidMessage = new Message("-1", "InvalidMessage", "404", "404");

    public Message(String id, String title, String type, String content) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public String getContent() {
        return content;
    }
    public boolean getIgnored() {
        return ignored;
    }
    public boolean getRead() {
        return read;
    }
    public boolean isNew() {
        return newMessage;
    }

    public void readMessage() {
        read = true;
    }

    public void ignoreMessage() {
        ignored = true;
    }
    public void setIsNew(boolean isNew) {
        newMessage = isNew;
    }

    /**
     * Always treat de-serialization as a full-blown constructor, by
     * validating the final state of the de-serialized object.
     */
    private void readObject(ObjectInputStream aInputStream)
            throws ClassNotFoundException, IOException {
        //always perform the default de-serialization first
        aInputStream.defaultReadObject();

        // serialize other fields
    }

    /**
     * This is the default implementation of writeObject.
     * Customise if necessary.
     */
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        //perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
    }
}
