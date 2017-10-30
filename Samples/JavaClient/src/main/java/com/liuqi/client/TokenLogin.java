package com.liuqi.client;

import okhttp3.*;

import java.io.IOException;

public class TokenLogin {
    public static void main(String[] args) {
        String loginUrl = "https://nxmup.com/api/dev/login";
        String token = "eyJhbGciOiJIUzI1NiIsImlhdCI6MTUwOTM2MDkwNywiZXhwIjoxNTA5MzY0NTA3fQ.eyJ1c2VybmFtZSI6MX0.2PdTswy6TER2EYD340vb5J4jiRce-r-1BciOdc9x5hk";

        TokenLogin tokenLogin = new TokenLogin();
        tokenLogin.login(loginUrl, token);
    }

    private void login(String url, String token) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Dev " + token)
                .build();
        Client okHttpClient = new Client();
        okHttpClient.handleResponse(client, request);
    }
}
