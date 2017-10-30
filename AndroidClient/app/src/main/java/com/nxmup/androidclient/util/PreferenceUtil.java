package com.nxmup.androidclient.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nxmup.androidclient.R;

public class PreferenceUtil {
    private static Context sContext;
    private static final String TOKEN = "Token";

    public static void init(Context context) {
        sContext = context;
    }

    private static SharedPreferences getInstance() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static void saveLastId(String id) {
        getInstance().edit().putString(sContext.getString(R.string.lastId), id).apply();
    }

    public static String getLastId() {
        return getInstance().getString(sContext.getString(R.string.lastId), null);
    }

    public static String getToken() {
        return getInstance().getString(TOKEN, null);
    }

    public static void saveToken(String token) {
        getInstance().edit().putString(TOKEN, token).apply();
    }
}
