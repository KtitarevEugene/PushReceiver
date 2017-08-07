package com.smartru.pushreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smartru.pushreceiver.FirebaseNotificationInstanceIDService;
import com.smartru.pushreceiver.http.ApiClient;
import com.smartru.pushreceiver.http.BaseResponse;
import com.smartru.pushreceiver.models.TokenModel;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        ConnectivityManager connection = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        /*if(info != null && info.isConnected()) {
            if (!pref.getBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, false) ||
                    pref.getString(FirebaseNotificationInstanceIDService.TOKEN, "") == "") {

                String token = FirebaseInstanceId.getInstance().getToken();
                if (token != null) {
                    BaseResponse response = ApiClient.sendToken(token, BaseResponse.class);
                    if (response.status == 200) {
                        pref.edit().putBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, true).apply();
                    } else {
                        pref.edit().putBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, false).apply();
                    }
                } else {
                    pref.edit().putBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, false).apply();
                }
                pref.edit().putString(FirebaseNotificationInstanceIDService.TOKEN, token).apply();
            }
        }*/
    }
}
