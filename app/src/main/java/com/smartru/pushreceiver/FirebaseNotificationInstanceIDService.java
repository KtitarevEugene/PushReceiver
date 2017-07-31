package com.smartru.pushreceiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.prefs.Preferences;

/**
 * Created by ektitarev on 28.07.17.
 */
public class FirebaseNotificationInstanceIDService extends FirebaseInstanceIdService {

    public static final String IS_CONNECTED = "is_connected";
    public static final String TOKEN = "token";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        sendToken(token);
    }

    private void sendToken(String token) {
        ConnectivityManager connection = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putString(TOKEN, token).apply();

        if (networkInfo != null && networkInfo.isConnected() && token != "") {

            // TODO sent token to api server

            pref.edit().putBoolean(IS_CONNECTED, true).apply();
        } else {
            pref.edit().putBoolean(IS_CONNECTED, false).apply();;
        }
    }
}
