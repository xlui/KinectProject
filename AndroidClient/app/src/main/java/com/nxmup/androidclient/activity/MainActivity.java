package com.nxmup.androidclient.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.adapter.FragmentAdapter;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.fragment.FirstTabFragment;
import com.nxmup.androidclient.fragment.SecondTabFragment;
import com.nxmup.androidclient.fragment.ThirdTabFragment;
import com.nxmup.androidclient.service.StateService;
import com.nxmup.androidclient.util.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageView icMenu;
    private DrawerLayout dlSlideMenu;
    private FrameLayout flTab1;
    private FrameLayout flTab2;
    private FrameLayout flTab3;
    private View tabSelect1;
    private View tabSelect2;
    private View tabSelect3;
    private ViewPager vpViewPager;
    private Fragment firstTab;
    private Fragment secondTab;
    private Fragment thirdTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
        initViews();
        setListener();
    }

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StateService stateService = ((StateService.SingleHolder) service).getStateService();
            AppCache.setStateService(stateService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindService() {

    }

    public void initViews() {
        icMenu = (ImageView) findViewById(R.id.iv_menu);
        dlSlideMenu = (DrawerLayout) findViewById(R.id.dl_slide_menu);
        flTab1 = (FrameLayout) findViewById(R.id.fl_tab1);
        flTab2 = (FrameLayout) findViewById(R.id.fl_tab2);
        flTab3 = (FrameLayout) findViewById(R.id.fl_tab3);
        tabSelect1 = findViewById(R.id.tab_select1);
        tabSelect2 = findViewById(R.id.tab_select2);
        tabSelect3 = findViewById(R.id.tab_select3);
        vpViewPager = (ViewPager) findViewById(R.id.vp_view_pager);

        firstTab = new FirstTabFragment();
        secondTab = new SecondTabFragment();
        thirdTab = new ThirdTabFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(firstTab);
        adapter.addFragment(secondTab);
        adapter.addFragment(thirdTab);
        vpViewPager.setAdapter(adapter);
        tabSelect1.setVisibility(View.VISIBLE);
    }

    private void setListener() {
        flTab1.setOnClickListener(this);
        flTab2.setOnClickListener(this);
        flTab3.setOnClickListener(this);
        vpViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_tab1:
                vpViewPager.setCurrentItem(0);
                break;
            case R.id.fl_tab2:
                vpViewPager.setCurrentItem(1);
                break;
            case R.id.fl_tab3:
                vpViewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabSelect1.setVisibility(View.VISIBLE);
                tabSelect2.setVisibility(View.GONE);
                tabSelect3.setVisibility(View.GONE);
                break;
            case 1:
                tabSelect2.setVisibility(View.VISIBLE);
                tabSelect1.setVisibility(View.GONE);
                tabSelect3.setVisibility(View.GONE);
                break;
            case 2:
                tabSelect3.setVisibility(View.VISIBLE);
                tabSelect2.setVisibility(View.GONE);
                tabSelect1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
