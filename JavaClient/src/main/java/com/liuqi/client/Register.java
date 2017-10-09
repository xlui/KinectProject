package com.liuqi.client;

import com.liuqi.json.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class Register {
    public static void main(String[] args) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final String registerUrl = "http://127.0.0.1:5000/api/dev/register";
        JSONObject user = new JSONObject();
        user.put("username", 2);
        user.put("password", "dev2");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, user.toString());
        Request request = new Request.Builder()
                .url(registerUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String responseData = responseBody.string();
                    System.out.println(responseData);
                }
            }
        });
    }
}
