package com.Activities.papa.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.NotificationCompat;

import com.Activities.papa.BundleHelper;
import com.Activities.papa.R;
import com.Back.NetworkAccess.papa.PapaHttpClientException;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManager;
import com.Back.PapaDataBaseManager.papa.PapaDataBaseManagerReal;
import com.Settings.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessagePullService extends Service {
    Timer pullingTimer;

    String userId;
    String token;

    PapaDataBaseManager papaDataBaseManager;

    public MessagePullService() {
        papaDataBaseManager = new PapaDataBaseManagerReal();
    }

    /**
     * the message list
     */
    public MessageList syncMessages() {

        // do nothing if userId or token do not exist
        if (userId == null || token == null)
            return new MessageList();

        // get local message list
        MessageList messageList = Settings.getInstance(this).getMessageList();

        // get remote message list
        // get all message ids
        List<String> messageIds;

        try {
            messageIds = papaDataBaseManager.getMessagesID(
                    new PapaDataBaseManager.GetMessagesIDRequest(Integer.parseInt(userId), token)
            ).msgIdLst;
        } catch (PapaHttpClientException e) {
            e.printStackTrace();
            // cannot get any messages.
            return new MessageList();
        }

        // filter those we have
        ArrayList<String> newMessageIds = messageList.filterByMessageId(messageIds);

        // get the messages that we do not have
        for (String id : newMessageIds) {
            try {
                Message message = papaDataBaseManager.getMessageByID(new PapaDataBaseManager.GetMessageByIDRequest(id, token)).msg;
                messageList.addFront(message);
            }
            catch (PapaHttpClientException e) {
                e.printStackTrace();
                // ignore the messages we can not download
            }
        }

        // mark all new messages as new
        MessageList list = filterNewMessages(messageList);
        if (list.size() > 0) {
            notifyMessageReceived(list);
        }

        // commit to the file
        Settings settings = Settings.getInstance(this);
        settings.setMessageList(messageList);
        settings.commit(this);

        // return
        return messageList;
    }

    /**
     * Synchronize the message list from outside the service.
     * @param list: the message list to synchronize.
     */
    public void syncFromApp(MessageList list) {
        Settings settings = Settings.getInstance(this);
        settings.setMessageList(list);
        settings.commit(this);
    }

    /**
     * @param id:   the ID of the current user(retrieved when logging in)
     * @param token: the authentication token for the current user
     */
    public void setUserInfo(String id, String token) {
        this.userId = id;
        this.token = token;
    }

    /**
     * The service starts a new thread and polls from the remote server.
     * If any new message are received, we will inform the user by Notification.
     */
    public void startListen() {
        if (pullingTimer != null)
            return;
        else
            pullingTimer = new Timer();

        pullingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                syncMessages();
            }
        }, 0, getResources().getInteger(R.integer.message_pull_service_polling_interval_milliseconds));
    }

    /**
     * Stop the background polling thread.
     */
    public void stopListen() {
        // NOTE: this method may cause an interrupted exception.
        pullingTimer.cancel();
    }


    /**
     * Create a notification if any message are nearing deadline.
     */
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
                            this, 0, createIntentForStartMessageActivity(), PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(getResources().getInteger(R.integer.key_message_pull_notification_id), notification);
        }
    }

    /**
     * Create a android Notification with messages.
     * @param messages the new messages.
     */
    public void notifyMessageReceived(MessageList messages) {
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
                        this, 0, createIntentForStartMessageActivity(), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(getResources().getInteger(R.integer.key_message_pull_notification_id), notification);
    }

    private Intent createIntentForStartMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        BundleHelper bundleHelper = new BundleHelper();
        bundleHelper.setId(Integer.parseInt(userId));
        bundleHelper.setToken(token);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.key_to_notification), bundleHelper);
        intent.putExtras(bundle);
        return intent;
    }
    // filter old messages
    public static MessageList filterNewMessages(MessageList list) {
        MessageList newList = new MessageList();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).isNew()) {
                list.get(i).setIsNew(false);
                newList.add(list.get(i));
            }
        }
        return newList;
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
        public MessagePullService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MessagePullService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
