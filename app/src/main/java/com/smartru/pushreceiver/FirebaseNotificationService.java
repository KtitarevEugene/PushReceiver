package com.smartru.pushreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smartru.pushreceiver.helpers.NotificationHelper;

import java.util.Map;

/**
 * Created by ektitarev on 28.07.17.
 */
public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String LOGCAT = FirebaseNotificationService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        if (data.containsKey("message") && data.containsKey("url")) {
            String message = data.get("message");
            String url = data.get("url");

            Log.d(LOGCAT, "message received : " + remoteMessage.getData());

            sendNotification(message, url);
        }
    }

    private void sendNotification(String message, String url) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Notification notification = NotificationHelper.makeNotification(this, message, url);

        notificationManager.notify(0, notification);
    }
}
