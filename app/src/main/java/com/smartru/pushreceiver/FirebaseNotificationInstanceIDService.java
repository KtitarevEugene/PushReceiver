package com.smartru.pushreceiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.smartru.pushreceiver.http.ApiClient;
import com.smartru.pushreceiver.http.BaseResponse;

/**
 * Created by ektitarev on 28.07.17.
 */
public class FirebaseNotificationInstanceIDService extends FirebaseInstanceIdService {

    private static final String LOGCAT = FirebaseNotificationInstanceIDService.class.getName();

    public static final String IS_CONNECTED = "is_connected";
    public static final String TOKEN = "token";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.d(LOGCAT, "onTokenRefresh");

        String token = FirebaseInstanceId.getInstance().getToken();
        //sendToken(token);
    }

    /*private void sendToken(String token) {

        ConnectivityManager connection = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putString(TOKEN, token).apply();

        if (networkInfo != null && networkInfo.isConnected() && token != "") {

            BaseResponse response = ApiClient.sendToken(token, BaseResponse.class);
            if (response.status == 200) {
                pref.edit().putBoolean(IS_CONNECTED, true).apply();
            } else {
                pref.edit().putBoolean(IS_CONNECTED, false).apply();
            }
        } else {
            pref.edit().putBoolean(IS_CONNECTED, false).apply();
        }
    }*/
}
