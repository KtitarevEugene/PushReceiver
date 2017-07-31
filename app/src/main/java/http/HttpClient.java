package http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by ektitarev on 31.07.17.
 */
public class HttpClient<V, T> {

    private static final String LOGCAT = HttpClient.class.getName();

    static final int TIMEOUT = 60000;

    public V sendPostRequest (String urlString, T data) {
        V response = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = getHttpURLConnection(url);
            writeRequest(connection, serializeJson(data));

            response = deserializeJson(readResponse(connection));

        } catch (Exception e) {
            Log.e(LOGCAT, e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(TIMEOUT);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        return connection;
    }

    private String serializeJson(T data) {
        return new Gson().toJson(data, data.getClass());
    }

    private V deserializeJson(String json) {
        //FIXME java.lang.ClassCastException here
        return new Gson().fromJson(json, (Class<V>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
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
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        return sb.toString();
    }
}
