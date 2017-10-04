package com.nxmup.androidclient.util;

import android.util.Base64;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void login(String id, String password, Callback callback) {
        String str = id + ":" + password;
        String base64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlBuilder.getStateUrl())
                .header("Authorization", "Basic "+base64.trim())
                .build();
        client.newCall(request).enqueue(callback);
    }
}
