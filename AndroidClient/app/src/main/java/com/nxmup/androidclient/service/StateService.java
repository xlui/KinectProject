package com.nxmup.androidclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class StateService extends Service {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public StateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SingleHolder();
    }

    public class SingleHolder extends Binder {
        public StateService getStateService() {
            return StateService.this;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    public void updateStatecConstantly() {
        mHandler.post(runnable);
    }
}
