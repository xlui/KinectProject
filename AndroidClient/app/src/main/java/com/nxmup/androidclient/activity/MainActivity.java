package com.nxmup.androidclient.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.adapter.FragmentAdapter;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.fragment.FirstTabFragment;
import com.nxmup.androidclient.fragment.SecondTabFragment;
import com.nxmup.androidclient.fragment.ThirdTabFragment;
import com.nxmup.androidclient.service.StateService;
import com.nxmup.androidclient.util.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageView ivMenu;
    private DrawerLayout dlSlideMenu;
    private ViewPager vpViewPager;
    private Fragment firstTab;
    private Fragment secondTab;
    private Fragment thirdTab;
    private TextView tvTab1;
    private TextView tvTab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
    }

    public void initViews() {
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        dlSlideMenu = (DrawerLayout) findViewById(R.id.dl_slide_menu);
        vpViewPager = (ViewPager) findViewById(R.id.vp_view_pager);
        tvTab1 = (TextView) findViewById(R.id.tv_tab_1);
        tvTab2 = (TextView) findViewById(R.id.tv_tab_2);
        firstTab = new FirstTabFragment();
        secondTab = new SecondTabFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(firstTab);
        adapter.addFragment(secondTab);

        vpViewPager.setAdapter(adapter);
        vpViewPager.setCurrentItem(0);
        tvTab1.setSelected(true);
    }

    private void setListener() {
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        vpViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_1:
                tvTab1.setSelected(true);
                tvTab2.setSelected(false);
                vpViewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab_2:
                tvTab1.setSelected(false);
                tvTab2.setSelected(true);
                vpViewPager.setCurrentItem(1);
                break;
            case R.id.iv_menu:
                dlSlideMenu.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvTab1.setSelected(true);
            tvTab2.setSelected(false);
        } else if (position == 1) {
            tvTab2.setSelected(true);
            tvTab1.setSelected(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
