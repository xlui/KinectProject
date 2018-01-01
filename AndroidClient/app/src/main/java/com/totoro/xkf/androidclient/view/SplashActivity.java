package com.totoro.xkf.androidclient.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity<SplashPresenter> {

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void initViews() {
        hideStateBar();
        TextView appName = findViewById(R.id.app_name);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(appName, "alpha", 0, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(mPresenter);
        objectAnimator.start();
    }
}
