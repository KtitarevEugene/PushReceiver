package com.smartru.pushreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import helpers.NotificationHelper;

/**
 * Implementation of App Widget functionality.
 */
public class main_widget extends AppWidgetProvider {

    private static final String LOGCAT = main_widget.class.getName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int widgetsCount = appWidgetIds.length;
        for (int i = 0; i < widgetsCount; ++i) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        int googleApiAvaibilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        switch(googleApiAvaibilityResult) {
            case ConnectionResult.SUCCESS:
                context.startService(new Intent(context, FirebaseNotificationInstanceIDService.class));
                context.startService(new Intent(context, FirebaseNotificationService.class));
                break;
            case ConnectionResult.SERVICE_MISSING:
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                break;
            case ConnectionResult.SERVICE_DISABLED:
                break;
        }

        String longText = "From Android Developer web site: Caution: If there are no apps on the device that can receive the implicit intent, your app will crash when it calls startActivity(). To first verify that an app exists to receive the intent, call resolveActivity() on your Intent object. If the result is non-null, there is at least one app that can handle the intent and it's safe to call startActivity(). If the result is null, you should not use the intent and, if possible, you should disable the feature that invokes the intent. ";
        String url = "http://yandex.ru";

        Notification notification = NotificationHelper.makeNotification(context, longText, url);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}

