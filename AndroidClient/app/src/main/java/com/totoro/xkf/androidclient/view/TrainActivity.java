package com.totoro.xkf.androidclient.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.TrainPresenter;

public class TrainActivity extends BaseActivity<TrainPresenter> {
    private Toolbar mToolbar;
    private SwipeRefreshLayout srlRefresh;
    private TextView tvTrainResult;
    private TextView tvTrainTarget;
    private TextView tvTrainDate;
    private TextView tvTrainDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_train;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrainPresenter(this);
    }

    @Override
    protected void initViews() {
        mToolbar = findViewById(R.id.tb_toolbar);
        setActionBar(mToolbar, 0, "康复计划");
        srlRefresh = findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(mPresenter);
        mPresenter.onRefresh();
    }

    public void startRefresh() {
        srlRefresh.setRefreshing(true);
    }

    public void stopRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(false);
            }
        });

    }

    public void setTargetText(final String date, final String target, int code) {
        switch (code) {
            case 1:
                tvTrainTarget = findViewById(R.id.tv1_train_target);
                tvTrainDate = findViewById(R.id.tv1_train_date);
                break;
            case 2:
                tvTrainTarget = findViewById(R.id.tv2_train_target);
                tvTrainDate = findViewById(R.id.tv2_train_date);
                break;
            case 3:
                tvTrainTarget = findViewById(R.id.tv3_train_target);
                tvTrainDate = findViewById(R.id.tv3_train_date);
                break;
            case 4:
                tvTrainTarget = findViewById(R.id.tv4_train_target);
                tvTrainDate = findViewById(R.id.tv4_train_date);
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTrainTarget.setText("预定目标："+target);
                tvTrainDate.setText(date);
            }
        });
    }

    public void setResultText(final String result, final String desc, int code) {
        switch (code) {
            case 1:
                tvTrainResult = findViewById(R.id.tv1_train_result);
                tvTrainDesc = findViewById(R.id.tv1_train_desc);
                break;
            case 2:
                tvTrainResult = findViewById(R.id.tv2_train_result);
                tvTrainDesc = findViewById(R.id.tv2_train_desc);
                break;
            case 3:
                tvTrainResult = findViewById(R.id.tv3_train_result);
                tvTrainDesc = findViewById(R.id.tv3_train_desc);
                break;
            case 4:
                tvTrainResult = findViewById(R.id.tv4_train_result);
                tvTrainDesc = findViewById(R.id.tv4_train_desc);
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTrainResult.setText("训练结果："+result);
                tvTrainDesc.setText("训练评价："+desc);
            }
        });
    }
}
