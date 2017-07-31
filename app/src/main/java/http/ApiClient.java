package http;

import models.TokenModel;

/**
 * Created by ektitarev on 31.07.17.
 */
public class ApiClient {
    public static void sendToken(String token) {
        String url = "";

        TokenModel model = new TokenModel();
        model.push_token = token;

        HttpClient<TokenModel> client = new HttpClient<>();
        client.sendPostRequest(url, model);
    }
}
