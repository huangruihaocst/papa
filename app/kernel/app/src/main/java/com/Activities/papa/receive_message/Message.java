package com.Activities.papa.receive_message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

/**
 * This class represents a message pulled from the server.
 * It is also serializable.
 */
public class Message implements Serializable {
    public enum Status {
        Critical,
        Normal,
        Outdated,
        Ignored,
        Read
    }

    // 2 days
    static final long TimeCriticalMilliseconds = 2 * 86400 * 1000;

    String id, title, type, content, creatorName, courseName;
    Calendar deadline;
    boolean ignored;
    boolean read;
    boolean newMessage;
    boolean notifyDeadline;

    public final static Message InvalidMessage = new Message("-1", "InvalidMessage", "404", "404", Calendar.getInstance(), "Operating System", "Alex");

    public Message(String id, String title, String type, String content, Calendar deadline, String courseName, String creatorName) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.content = content;
        this.deadline = deadline;
        this.courseName = courseName;
        this.creatorName = creatorName;
        ignored = false;
        read = false;
        newMessage = true;
        notifyDeadline = true;
    }

    // getters
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
    public String getTruncatedContent(int length) {
        if (content.length() > length) {
            return content.substring(0, length);
        }
        else {
            return content;
        }
    }

    public Calendar getDeadline() {
        return deadline;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getCreatorName() {
        return creatorName;
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
    public boolean needNotifyDeadline() {
        return notifyDeadline;
    }

    public Status getStatus() {
        if (ignored)
            return Status.Ignored;
        long delta = getDeadline().getTimeInMillis() - System.currentTimeMillis();
        if (delta <= 0)
            return Status.Outdated;
        else if (delta < TimeCriticalMilliseconds)
            return Status.Critical;
        if (read)
            return Status.Read;
        return Status.Normal;
    }

    // these functions will affect message contents.
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
