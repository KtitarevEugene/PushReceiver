package com.smartru.pushreceiver;

import android.app.NotificationManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ektitarev on 28.07.17.
 */
public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String LOGCAT = FirebaseNotificationService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);


        Log.d(LOGCAT, "message received : " + remoteMessage.getNotification().getBody());

    }
}
