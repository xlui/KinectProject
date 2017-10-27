package com.nxmup.androidclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.nxmup.androidclient.listener.OnStateChangeListener;
import com.nxmup.androidclient.util.HttpUtil;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.UrlBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StateService extends Service {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private OnStateChangeListener onStateChangeListener;
    private String id;
    private String password;

    public StateService() {
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HttpUtil.updateState(getCurrentId(), getCurrentPassword(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject state = jsonObject.optJSONObject("state");
                        String newState = state.optString("state");
                        onStateChangeListener.onStateChange(newState);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mHandler.postDelayed(runnable, 5000);
        }
    };

    public void updateStateConstantly() {
        mHandler.post(runnable);
    }

    public void stopUpdateState() {
        mHandler.removeCallbacks(runnable);
    }

    public void setCurrentIdAndPassword(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getCurrentId() {
        return id;
    }

    public String getCurrentPassword() {
        return password;
    }
}
