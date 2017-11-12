package com.nxmup.androidclient.activity;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.service.StateService;

public class RecoverTaskActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout dlRTDrawerLayout;
    private RecyclerView rvHistoryTrainState;
    private NavigationView nvRecoverTaskNavigationView;
    private TextView tvNowStateUserId;
    private Handler handler = new Handler();
    private boolean canFinish = false;
    private StateService mStateService;
    private SwipeRefreshLayout srlRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_task);
        mStateService = AppCache.getStateService();
        initViews();
    }

    private void initViews() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_recover_task_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("康复计划");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_more);
        }
        dlRTDrawerLayout = (DrawerLayout) findViewById(R.id.dl_rt_drawer_layout);
        rvHistoryTrainState = (RecyclerView) findViewById(R.id.rv_history_train_state);
        srlRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh_layout);
        srlRefreshLayout.setOnRefreshListener(this);

        nvRecoverTaskNavigationView = (NavigationView) findViewById(R.id.nv_recover_task_navigation_view);
        View view = nvRecoverTaskNavigationView.getHeaderView(0);
        tvNowStateUserId = view.findViewById(R.id.tv_now_state_user_id);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            dlRTDrawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (dlRTDrawerLayout.isDrawerOpen(Gravity.START)) {
            dlRTDrawerLayout.closeDrawers();
        } else if (canFinish) {
            System.exit(0);
        } else if (!canFinish) {
            canFinish = true;
            Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canFinish = false;
                }
            }, 3000);
        }
    }

    @Override
    public void onRefresh() {
        srlRefreshLayout.setRefreshing(true);
        srlRefreshLayout.setRefreshing(false);
    }
}
