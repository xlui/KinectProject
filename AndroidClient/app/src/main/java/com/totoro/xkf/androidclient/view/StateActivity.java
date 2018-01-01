package com.totoro.xkf.androidclient.view;

import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.totoro.xkf.androidclient.fragment.LookStateFragment;
import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.StatePresenter;

import java.util.ArrayList;
import java.util.List;

public class StateActivity extends BaseActivity<StatePresenter> {
    public TabLayout stateTabLayout;
    public ViewPager stateViewPager;
    public Toolbar stateToolbar;
    public DrawerLayout stateDrawerLayout;
    public NavigationView stateNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_state;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new StatePresenter(this);
    }

    @Override
    protected void initViews() {
        stateToolbar = findViewById(R.id.state_toolbar);
        setActionBar(stateToolbar, R.mipmap.menu, "实时状态");

        stateTabLayout = findViewById(R.id.state_tab_layout);
        stateViewPager = findViewById(R.id.state_view_pager);
        stateTabLayout.setTabMode(TabLayout.MODE_FIXED);
        stateViewPager.setAdapter(mPresenter.createAdapter());
        stateTabLayout.setupWithViewPager(stateViewPager);
        stateTabLayout.getTabAt(0).setText("实时检测");
        stateTabLayout.getTabAt(1).setText("实时图像");

        stateDrawerLayout = findViewById(R.id.state_drawer_layout);
        stateNavigationView = findViewById(R.id.state_navigation_view);
        stateNavigationView.setNavigationItemSelectedListener(mPresenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stateDrawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mPresenter.canClose()){
            System.exit(0);
        }
    }
}
