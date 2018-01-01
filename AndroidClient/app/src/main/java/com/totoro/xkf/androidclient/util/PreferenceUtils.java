package com.totoro.xkf.androidclient.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    private static Context sContext;
    private static final String TOKEN = "Token";

    public static void init(Context context) {
        sContext = context;
    }

    private static SharedPreferences getInstance() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static String getToken() {
        return getInstance().getString(TOKEN, null);
    }

    public static void saveToken(String token) {
        getInstance().edit().putString(TOKEN, token).apply();
    }
}
