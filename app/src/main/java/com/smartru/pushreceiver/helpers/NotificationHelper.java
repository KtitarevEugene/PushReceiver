package com.smartru.pushreceiver.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.smartru.pushreceiver.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ektitarev on 31.07.17.
 */
public class NotificationHelper
{
    public static Notification makeNotification(Context context, String message, String url) {
        PendingIntent pendingIntent = makePendingIntent(context, url);

        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context)
            .setContentTitle(context.getResources().getString(R.string.notification_title))
            .setSound(ringtone)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(message))
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build();
    }

    private static PendingIntent makePendingIntent(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        url = checkProtocol(url);
        intent.setData(Uri.parse(url));

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static String checkProtocol(String url) {
        Pattern regex = Pattern.compile("^https?://");
        Matcher matcher = regex.matcher(url);
        if(!matcher.find()) {
            return "http://" + url;
        }

        return url;
    }
}
