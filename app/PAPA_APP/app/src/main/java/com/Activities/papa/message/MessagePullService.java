package com.Activities.papa.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.Activities.papa.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

public class MessagePullService extends Service {

    /** This thread will poll messages from the server
     *      and call the service function onMessageReceived
     *      if new Message received.
     */
    class PullingThread extends Thread {
        public Handler mHandler;
        public static final int Stop = 1;
        public static final int Pull = 2;
        public static final int Start = 3;

        public static final int PullIntervalMilliseconds = 5000;

        public void run() {
            Looper.prepare();

            final java.util.Timer timer = new java.util.Timer(true);
            final TimerTask task = new TimerTask() {
                public void run() {
                    android.os.Message m = new Message();
                    m.what = Pull;
                    mHandler.dispatchMessage(m);
                }
            };

            mHandler = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case Start:
                            timer.schedule(task, 0, PullIntervalMilliseconds);
                            break;
                        case Stop:
                            timer.cancel();
                            break;
                        case Pull:
                            MessageList list = readNewMessages(syncMessages());
                            if (list.size() > 0) {
                                onMessageReceived(list);
                            }
                            break;
                    }
                }
            };

            Message msg = new Message();
            msg.what = Start;
            mHandler.dispatchMessage(msg);

            Looper.loop();
        }

    }

    PullingThread pullingThread;

    static int messageCount = 0;

    // TODO get real messages
    public MessageList syncMessages() {
        // get local message list
        MessageList messageList = new MessageList();
        try {
            File file = new File(getFilesDir().getPath() + "/" + getString(R.string.key_message_service_messages_file_name));
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            messageList = (MessageList) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        // get remote message list
        // get all message ids
        ArrayList<String> allIds = new ArrayList<>();
        allIds.add(String.valueOf(messageCount));

        // filter those we have
        ArrayList<String> dontHave = messageList.filterByMessageId(allIds);

        // get those we do not have
        // GET /messages/donthave.json
        Calendar deadline = Calendar.getInstance();
        deadline.add(Calendar.SECOND, 20);
        com.Activities.papa.message.Message msg =
                new com.Activities.papa.message.Message(
                        String.valueOf(messageCount),
                        String.valueOf(messageCount) + " days off!",
                        "notification",
                        "It's real!",
                        deadline,
                        "Operating System",
                        "Alex"
                        );
        messageList.add(0, msg);

        messageCount++;

        // save
        try {
            File file = new File(getFilesDir().getPath() + "/" + getString(R.string.key_message_service_messages_file_name));
            FileOutputStream fos = new FileOutputStream(file, true);
            FileChannel channel = fos.getChannel();
            channel.lock();
            channel.truncate(0);
            channel.force(true);
            fos.close();

            fos = new FileOutputStream(file);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(messageList);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return
        return messageList;
    }

    //
    public void syncFromApp(MessageList list) {
        try {
            File file = new File(getFilesDir().getPath() + "/" + getString(R.string.key_message_service_messages_file_name));
            FileOutputStream fos = new FileOutputStream(file, true);
            FileChannel channel = fos.getChannel();
            channel.lock();
            channel.truncate(0);
            channel.force(true);
            fos.close();

            fos = new FileOutputStream(file);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(list);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // filter old messages
    public MessageList readNewMessages(MessageList list) {
        MessageList newList = new MessageList();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).isNew()) {
                list.get(i).setIsNew(false);
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    // DONE
    public void startListen() {
        stopListen();

        pullingThread = new PullingThread();
        pullingThread.start();
    }

    // DONE
    public void stopListen() {
        if (pullingThread != null) {
            android.os.Message message = new android.os.Message();
            message.what = PullingThread.Stop;
            pullingThread.mHandler.dispatchMessage(message);
            while (pullingThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // DONE
    public void clearMessageCache() {
        File file = new File(getFilesDir().getPath() + "/" + getString(R.string.key_message_service_messages_file_name));
        // just to make the IDE happy
        boolean deleted = file.delete();
        if (!deleted) {
            throw new IOError(new IOException("Can't delete message cache file"));
        }
    }

    public void notifyMessagesNearingDeadline() {
        MessageList messageList = syncMessages();
        MessageList near = new MessageList();
        for (int i = 0; i < messageList.size(); ++i) {
            long delta = messageList.get(i).getDeadline().getTimeInMillis() - System.currentTimeMillis();
            if (!messageList.get(i).getIgnored() && messageList.get(i).needNotifyDeadline() && delta > 0 && delta < getResources().getInteger(R.integer.min_deadline_warning_in_milliseconds)) {
                near.add(messageList.get(i));
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < near.size(); ++i) {
            sb.append(near.get(i).getTitle()).append(" ");
        }

        if (near.size() > 0) {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.string_message_notify_deadline_title, near.size()))
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentText(sb.toString())
                    .setContentIntent(PendingIntent.getActivity(
                            this, 0, new Intent(this, MessageActivity.class), 0))
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(getResources().getInteger(R.integer.key_message_nearing_deadline_notification_id), notification);
        }
    }

    // DONE
    public void onMessageReceived(MessageList messages) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < messages.size(); ++i) {
            builder.append(messages.get(i).getTitle());
            builder.append("\n");
        }

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("New " + messages.size() + " Messages!")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentText(builder.toString())
                .setContentIntent(PendingIntent.getActivity(
                        this, 0, new Intent(this, MessageActivity.class), 0))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(getResources().getInteger(R.integer.key_message_pull_notification_id), notification);
    }

    /**
     * Binder given to clients
     */
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        MessagePullService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MessagePullService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
