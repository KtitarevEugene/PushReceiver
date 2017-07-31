package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smartru.pushreceiver.FirebaseNotificationInstanceIDService;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        ConnectivityManager connection = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            if (!pref.getBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, false) ||
                    pref.getString(FirebaseNotificationInstanceIDService.TOKEN, "") == "") {

                String token = FirebaseInstanceId.getInstance().getToken();

                // TODO send token to api server

                pref.edit().putString(FirebaseNotificationInstanceIDService.TOKEN, token);
                pref.edit().putBoolean(FirebaseNotificationInstanceIDService.IS_CONNECTED, true);
            }
        }
    }
}
