package com.totoro.xkf.androidclient.base;

public abstract class BasePresenter<T extends BaseActivity> {
    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
    }
}
