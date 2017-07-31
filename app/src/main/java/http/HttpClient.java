package http;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ektitarev on 31.07.17.
 */
public class HttpClient<T extends Object> {

    static final int TIMEOUT = 60000;

    public void sendPostRequest (String urlString, T data) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = getHttpURLConnection(url);
            writeRequest(connection, convertToJson(data));

            int status = connection.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(TIMEOUT);
        connection.setDoOutput(true);

        return connection;
    }

    private String convertToJson(T data) {
        return new Gson().toJson(data, data.getClass());
    }

    private void writeRequest(HttpURLConnection connection, String json) throws IOException {
        DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());

        outStream.writeBytes(json);
        outStream.flush();
        outStream.close();
    }
}
