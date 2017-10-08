package com.liuqi.client;

import com.liuqi.json.JSONObject;
import okhttp3.*;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        String rootUrl = "http://111.231.1.210";
        String latestUrl = rootUrl + "/api/dev";
        String updateUrl = rootUrl + "/api/dev/update";

        String username = "1";
        String password = "dev";
        String authorization = new BASE64Encoder().encode((username + ":" + password).getBytes());

        // JSON 格式化的数据。state 为 key，必需。
        JSONObject handState = new JSONObject();
        handState.put("state", "TEST_STATE");

        Client client = new Client();
        client.getLatest(latestUrl, authorization);
//        client.post(updateUrl, handState, authorization);
    }

    private void getLatest(String url, String authorization) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Basic " + authorization)
                .build();

        handleResponse(client, request);
    }

    private void post(String url, JSONObject state, String authorization) {
        // 向服务器发送数据，仅需发送手势即可。
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, state.toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Basic " + authorization)
                .post(body)
                .build();

        handleResponse(client, request);
    }

    private void handleResponse(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody response_body = response.body();
                    if (response_body != null) {
                        String responseData = response_body.string();
                        System.out.println(responseData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
