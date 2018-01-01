package com.totoro.xkf.androidclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.AllHandStatePresenter;
import com.totoro.xkf.androidclient.util.StateSelector;

public class AllHandStateActivity extends BaseActivity<AllHandStatePresenter> {
    private Toolbar allStateToolbar;
    private ListView allStateListView;

    @Override
    protected int getLayout() {
        return R.layout.activity_all_hand_state;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AllHandStatePresenter(this);
    }

    @Override
    protected void initViews() {
        allStateToolbar = findViewById(R.id.all_state_toolbar);
        allStateListView = findViewById(R.id.all_state_listview);
        setActionBar(allStateToolbar, 0, "全手势说明");
        allStateListView.setAdapter(mPresenter.createAdapter());
    }
}
