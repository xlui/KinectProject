package com.totoro.xkf.androidclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.HistoryHandStatePresenter;

import java.util.List;

public class HistoryHandStateActivity extends BaseActivity<HistoryHandStatePresenter> {
    private Toolbar historyToolbar;
    private ListView historyListView;

    @Override
    protected int getLayout() {
        return R.layout.activity_history_hand_state;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HistoryHandStatePresenter(this);
    }

    @Override
    protected void initViews() {
        historyToolbar = findViewById(R.id.history_toolbar);
        historyListView = findViewById(R.id.history_listview);
        setActionBar(historyToolbar,0,"历史记录");
        mPresenter.updateHistoryData();
    }

    public void updateListView(final String[] historyData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (HistoryHandStateActivity.this, android.R.layout.simple_list_item_1, historyData);
                historyListView.setAdapter(adapter);
            }
        });
    }
}
