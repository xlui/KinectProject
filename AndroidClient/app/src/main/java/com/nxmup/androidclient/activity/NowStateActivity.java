package com.nxmup.androidclient.activity;

import android.content.Intent;
import android.os.Vibrator;
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
import com.nxmup.androidclient.util.HttpUtil;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.NotificationUtil;
import com.nxmup.androidclient.util.PreferenceUtil;
import com.nxmup.androidclient.util.StateSelector;
import com.nxmup.androidclient.util.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NowStateActivity extends AppCompatActivity implements OnStateChangeListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dlDrawerLayout;
    private TextView tvNowStateInfo;
    private ImageView ivNowStateImage;
    private StateService mStateService;
    private TextView tvDetail;
    private TextView tvUpdateTime;
    private RecyclerView rvNowStateHistoryStateList;
    private NavigationView nvNowStateNavigationView;
    private TextView tvNowStateUserId;
    private Vibrator vibrator;

    private String lastString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_state);
        mStateService = AppCache.getStateService();
        mStateService.setOnStateChangeListener(this);
        mStateService.updateStateConstantly();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        initViews();
    }

    private void initViews() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_now_state_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.nowState));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_more);
        }

        dlDrawerLayout = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
        ivNowStateImage = (ImageView) findViewById(R.id.iv_now_state_image);
        tvNowStateInfo = (TextView) findViewById(R.id.tv_now_state_info);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);

        rvNowStateHistoryStateList = (RecyclerView) findViewById(R.id.rv_now_state_history_state_list);
        HttpUtil.getHistoryState(PreferenceUtil.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final List<String> history = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state = jsonObject.optString("state");
                        String date = jsonObject.optString("date");
                        history.add(state + "@" + date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HistoryStateListAdapter adapter = new HistoryStateListAdapter(history);
                        LinearLayoutManager manager = new LinearLayoutManager(NowStateActivity.this);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rvNowStateHistoryStateList.setAdapter(adapter);
                        rvNowStateHistoryStateList.setLayoutManager(manager);
                    }
                });
            }
        });

        nvNowStateNavigationView = (NavigationView) findViewById(R.id.nv_now_state_navigation_view);
        View view = nvNowStateNavigationView.getHeaderView(0);
        tvNowStateUserId = view.findViewById(R.id.tv_now_state_user_id);
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
    public void onStateChange(final String newState, final boolean isDanger) {
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
                    tvNowStateUserId.setText("当前用户ID   " + mStateService.getCurrentId());
                    if (isDanger) {
                        vibrator.vibrate(2000);
                        mStateService.getDangerHandStateList().add(time + "@" + newState + "@" + stateDetail);
                        NotificationUtil.createDangerNotification(NowStateActivity.this, "检测到危险手势！！！",
                                time + " 检测到危险手势" + newState + " " + stateDetail);
                    }
                }
                Glide.with(NowStateActivity.this).load(UrlBuilder.getStateImageUrl(newState)).into(ivNowStateImage);
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
            case R.id.look_man_state:
                startActivity(new Intent(this, ManStateActivity.class));
                break;
        }
        return true;
    }
}
