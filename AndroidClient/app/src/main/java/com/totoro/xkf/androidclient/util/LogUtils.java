package com.totoro.xkf.androidclient.util;

import android.util.Log;

public class LogUtils {
    public static void show() {
        Log.e("xkf", "this");
    }

    public static void show(String message) {
        Log.e("xkf", "show: " + message);
    }
}
