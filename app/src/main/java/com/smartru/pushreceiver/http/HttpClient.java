package com.smartru.pushreceiver.http;

import android.util.Log;

import com.google.gson.Gson;
import com.smartru.pushreceiver.models.ResponseModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ektitarev on 31.07.17.
 */
public class HttpClient<V, T> {

    private static final String LOGCAT = HttpClient.class.getName();

    private String apiHost;

    public HttpClient(String host) {
        apiHost = host;
    }

    public V sendPostRequest (String urlString, T data, Class classObj) {
        V response = null;
        try {
            URL url = new URL(String.format("%s%s", apiHost, urlString));

            HttpURLConnection connection = getHttpURLConnection(url);

            writeRequest(connection, serializeJson(data));

            String serializedJson = readResponse(connection);
            ResponseModel responseModel = new Gson().fromJson(serializedJson, ResponseModel.class);

            response = (V)deserializeJson(responseModel.d, classObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(ApiConfig.TIMEOUT);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        return connection;
    }

    private String serializeJson(Object data) {
        return new Gson().toJson(data, data.getClass());
    }

    private V deserializeJson(String json, Class<V> tClass) {
        Log.d(LOGCAT, json);
        return new Gson().fromJson(json, tClass);
    }

    private void writeRequest(HttpURLConnection connection, String json) throws IOException {
        DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());

        outStream.writeBytes(json);
        outStream.flush();
        outStream.close();
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        return sb.toString();
    }
}
