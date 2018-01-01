package com.totoro.xkf.androidclient.app;

import android.app.Application;
import android.preference.Preference;

import com.totoro.xkf.androidclient.util.PreferenceUtils;


public class KinectApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
    }
}
