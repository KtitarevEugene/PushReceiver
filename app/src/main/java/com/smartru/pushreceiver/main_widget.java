package com.smartru.pushreceiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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
    }
}

