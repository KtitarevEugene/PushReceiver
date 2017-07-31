package helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;

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

        Notification notification = new Notification.Builder(context)
            .setContentTitle(context.getResources().getString(R.string.notification_title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
            .setStyle(new Notification.BigTextStyle()
                    .bigText(message))
            .setContentIntent(pendingIntent)
            .build();
        return notification;
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
