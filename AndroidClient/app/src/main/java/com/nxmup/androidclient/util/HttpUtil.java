package com.nxmup.androidclient.util;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void login(String id, String password, Callback callback) {
        String str = id + ":" + password;
        String base64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlBuilder.getStateUrl())
                .header("Authorization", "Basic " + base64.trim())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void register(String id, String password, Callback callback) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final String registerUrl = UrlBuilder.getRegisterUrl();
        int userId = Integer.parseInt(id);
        JSONObject user = new JSONObject();
        try {
            user.put("username", userId);
            user.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, user.toString());
        Request request = new Request.Builder()
                .url(registerUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateState(String id, String password, Callback callback) {
        String str = id + ":" + password;
        String base64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlBuilder.getUpdateStateUrl())
                .header("Authorization", "Basic " + base64.trim())
                .build();
        client.newCall(request).enqueue(callback);
    }
}
