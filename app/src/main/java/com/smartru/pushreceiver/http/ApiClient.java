package com.smartru.pushreceiver.http;

import com.smartru.pushreceiver.models.TokenModel;

/**
 * Created by ektitarev on 31.07.17.
 */
public class ApiClient {

    public static <V> V sendToken(TokenModel token, Class classObj) {
        String url = "Services/AppData.asmx/pushtoken";

        HttpClient<V,TokenModel> client = new HttpClient<>(ApiConfig.API_HOST);
        return client.sendPostRequest(url, token, classObj);
    }
}
