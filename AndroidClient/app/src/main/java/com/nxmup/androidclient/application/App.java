package com.nxmup.androidclient.application;

import android.app.Application;

import com.nxmup.androidclient.util.PreferenceUtil;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.init(getApplicationContext());
    }
}
