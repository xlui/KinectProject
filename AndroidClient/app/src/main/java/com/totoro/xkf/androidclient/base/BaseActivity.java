package com.totoro.xkf.androidclient.base;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.totoro.xkf.androidclient.R;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    protected boolean isLife = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initPresenter();
        initViews();
    }

    protected abstract int getLayout();

    protected abstract void initPresenter();

    protected void setActionBar(Toolbar toolbar, int imageId, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if (imageId != -1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (imageId != 0) {
                actionBar.setHomeAsUpIndicator(imageId);
            }
        }
    }

    protected abstract void initViews();

    protected void hideStateBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    protected boolean isLife() {
        return isLife;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLife = false;
    }
}
