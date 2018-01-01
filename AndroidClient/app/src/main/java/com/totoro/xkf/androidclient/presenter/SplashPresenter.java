package com.totoro.xkf.androidclient.presenter;


import android.animation.Animator;
import android.content.Intent;
import android.view.animation.Animation;

import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.util.HttpUtils;
import com.totoro.xkf.androidclient.util.PreferenceUtils;
import com.totoro.xkf.androidclient.view.LoginActivity;
import com.totoro.xkf.androidclient.view.SplashActivity;
import com.totoro.xkf.androidclient.view.StateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashPresenter extends BasePresenter<SplashActivity> implements Animator.AnimatorListener {

    public SplashPresenter(SplashActivity mView) {
        super(mView);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

        String token = PreferenceUtils.getToken();
        if (token != null) {
            HttpUtils.login(token, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String result = jsonObject.optString("login");
                        if (result.equals("success")) {
                            mView.startActivity(new Intent(mView, StateActivity.class));
                            mView.finish();
                        } else if (result.equals("failed")) {
                            mView.startActivity(new Intent(mView, LoginActivity.class));
                            PreferenceUtils.saveToken(null);
                            mView.startActivity(new Intent(mView, LoginActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            mView.startActivity(new Intent(mView, LoginActivity.class));
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
