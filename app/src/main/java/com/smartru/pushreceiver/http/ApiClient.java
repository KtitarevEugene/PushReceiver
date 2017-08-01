package com.smartru.pushreceiver.http;

import com.smartru.pushreceiver.models.TokenModel;

/**
 * Created by ektitarev on 31.07.17.
 */
public class ApiClient {

    public static <V> V sendToken(String token, Class classObj) {
        String url = "pushtoken";

        TokenModel model = new TokenModel();
        model.push_token = token;

        HttpClient<V,TokenModel> client = new HttpClient<>(ApiConfig.API_HOST);
        return client.sendPostRequest(url, model, classObj);
    }
}
