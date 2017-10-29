package com.nxmup.androidclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nxmup.androidclient.R;
import com.nxmup.androidclient.adapter.HistoryStateListAdapter;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.listener.OnStateChangeListener;
import com.nxmup.androidclient.service.StateService;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.StateSelector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowStateActivity extends AppCompatActivity implements OnStateChangeListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dlDrawerLayout;
    private TextView tvNowStateInfo;
    private ImageView ivNowStateImage;
    private StateService mStateService;
    private TextView tvDetail;
    private TextView tvUpdateTime;
    private RecyclerView rvNowStateHistoryStateList;
    private NavigationView nvNowStateNavigationView;

    private static final String picUrl = "https://nxmup.com/static/images/test.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_state);
        mStateService = AppCache.getStateService();
        mStateService.setOnStateChangeListener(this);
        mStateService.updateStateConstantly();
        initViews();
    }

    private void initViews() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_now_state_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.nowState));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        dlDrawerLayout = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
        ivNowStateImage = (ImageView) findViewById(R.id.iv_now_state_image);
        tvNowStateInfo = (TextView) findViewById(R.id.tv_now_state_info);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);

        rvNowStateHistoryStateList = (RecyclerView) findViewById(R.id.rv_now_state_history_state_list);
        HistoryStateListAdapter adapter = new HistoryStateListAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNowStateHistoryStateList.setAdapter(adapter);
        rvNowStateHistoryStateList.setLayoutManager(manager);

        nvNowStateNavigationView = (NavigationView) findViewById(R.id.nv_now_state_navigation_view);
        View view = nvNowStateNavigationView.getHeaderView(0);
        TextView tvNowStateUserId = view.findViewById(R.id.tv_now_state_user_id);
        tvNowStateUserId.setText("当前用户ID   " + mStateService.getCurrentId());
        nvNowStateNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            dlDrawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }

    @Override
    public void onStateChange(final String newState) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!newState.equals(AppCache.getStateService().getCurrentState())) {
                    AppCache.getStateService().setCurrentState(newState);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String time = formatter.format(curDate);
                    tvNowStateInfo.setText(newState);
                    String stateDetail = StateSelector.getState(newState);
                    if (TextUtils.isEmpty(stateDetail)) {
                        tvDetail.setText("");
                    } else {
                        tvDetail.setText(stateDetail);
                    }
                    tvUpdateTime.setText("最新手势获取于" + time);
                }
                Glide.with(NowStateActivity.this).load(picUrl).into(ivNowStateImage);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dlDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.all_state_info:
                startActivity(new Intent(this, HandStateInfoActivity.class));
                break;
        }
        return true;
    }


}
