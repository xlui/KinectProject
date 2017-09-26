package com.nxmup.androidclient.util;

import android.util.Log;

public class LogUtil {
    public static void show() {
        Log.e("xkf", "this");
    }

    public static void show(String message) {
        Log.e("xkf", "show: " + message);
    }
}
