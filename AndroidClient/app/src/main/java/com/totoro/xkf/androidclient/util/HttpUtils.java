package com.totoro.xkf.androidclient.util;

import android.net.Uri;
import android.util.Base64;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {
    public static void login(String id, String password, Callback callback) {
        sendRequest(id, password, callback, UrlBuilder.getLoginUrl());
    }

    public static void getToken(String id, String password, Callback callback) {
        sendRequest(id, password, callback, UrlBuilder.getTokenUrl());
    }

    public static void login(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getLoginUrl());
    }

    public static void updateState(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getUpdateStateUrl());
    }

    public static void getHistoryState(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getHistoryStateUrl());
    }

    public static void getPicture(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getPictureUrl());
    }

    public static void getTrainTarget(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getTrainTargetUrl());
    }

    public static void getTrainResult(String token, Callback callback) {
        sendRequest(token, callback, UrlBuilder.getTrainResultUrl());
    }

    private static void sendRequest(String id, String password, Callback callback, String url) {
        String str = id + ":" + password;
        String base64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Basic " + base64.trim())
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static void sendRequest(String token, Callback callback, String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Dev " + token)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
