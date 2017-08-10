package com.smartru.pushreceiver.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smartru.pushreceiver.FirebaseNotificationInstanceIDService;
import com.smartru.pushreceiver.R;
import com.smartru.pushreceiver.http.ApiClient;
import com.smartru.pushreceiver.http.BaseResponse;
import com.smartru.pushreceiver.models.TokenModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOGCAT = MainActivity.class.getName();

    private EditText userNameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userNameField = (EditText)findViewById(R.id.user_name);
        passwordField =  (EditText)findViewById(R.id.password);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String username = pref.getString(FirebaseNotificationInstanceIDService.USER_NAME, "");
        String password = pref.getString(FirebaseNotificationInstanceIDService.PASSWORD, "");

        if (!username.isEmpty() && !password.isEmpty()) {
            userNameField.setText(username);
            passwordField.setText(password);
        }

        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        String message = checkGoogleServicesAvaibility();
        if(message.isEmpty()) {
            String userName = userNameField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();

            String validateMessage = validateFields(userName, pass);
            if (validateMessage.isEmpty()) {
                submit(userName, pass);
            } else {
                createErrorDialog(validateMessage).show();
            }
        } else {
            createErrorDialog(message).show();
        }
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    private String checkGoogleServicesAvaibility() {
        int googleApiAvaibilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        switch(googleApiAvaibilityResult) {
            case ConnectionResult.SUCCESS:
                return "";
            case ConnectionResult.SERVICE_MISSING:
                return getResources().getString(R.string.toast_missing);
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                return getResources().getString(R.string.toast_update);
            case ConnectionResult.SERVICE_DISABLED:
                return getResources().getString(R.string.toast_disabled);
            default:
                return getResources().getString(R.string.unknown_issue_message);
        }
    }

    private String validateFields(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return getResources().getString(R.string.validate_error_message);
        }
        return "";
    }

    private AlertDialog createErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.error_title))
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void submit(final String userName, final String password) {
        AsyncTask<TokenModel, Void, BaseResponse> task = new AsyncTask<TokenModel, Void, BaseResponse>() {
            private ProgressDialog progressDialog;
            private TokenModel model;

            @Override
            protected BaseResponse doInBackground(TokenModel... params) {
                model = params[0];
                if (model != null) {
                    model.push_token = FirebaseInstanceId.getInstance().getToken();
                    return sendToken(model);
                }
                return null;
            }
            private BaseResponse sendToken(TokenModel model) {

                ConnectivityManager connection = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connection.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() && !model.push_token.isEmpty()) {
                    return ApiClient.sendToken(model, BaseResponse.class);
                }
                BaseResponse response = new BaseResponse();
                response.status = -1;
                if (networkInfo == null || !networkInfo.isConnected()) {
                    response.error = getResources().getString(R.string.no_connection_message);
                } else if (model.push_token.isEmpty()) {
                    response.error = getResources().getString(R.string.token_issue_message);
                } else {
                    response.error = getResources().getString(R.string.unknown_issue_message);
                }
                return response;
            }

            private void saveData() {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                pref.edit().putString(FirebaseNotificationInstanceIDService.TOKEN, model.push_token)
                        .putString(FirebaseNotificationInstanceIDService.USER_NAME, model.username)
                        .putString(FirebaseNotificationInstanceIDService.PASSWORD, model.password).apply();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(BaseResponse response) {
                super.onPostExecute(response);

                progressDialog.cancel();

                if (response != null) {
                    if (response.status == 200) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                        saveData();
                    } else {
                        createErrorDialog(response.error).show();
                    }
                } else {
                    createErrorDialog(getResources().getString(R.string.unknown_issue_message)).show();
                }
            }
        };

        TokenModel model = new TokenModel();
        model.username = userName;
        model.password = password;

        task.execute(model);
    }
}
