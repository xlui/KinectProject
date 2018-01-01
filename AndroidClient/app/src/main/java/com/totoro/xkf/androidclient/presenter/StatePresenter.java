package com.totoro.xkf.androidclient.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.app.AppCache;
import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.fragment.LookStateFragment;
import com.totoro.xkf.androidclient.fragment.StateImageFragment;
import com.totoro.xkf.androidclient.util.HttpUtils;
import com.totoro.xkf.androidclient.util.LogUtils;
import com.totoro.xkf.androidclient.util.NotificationUtils;
import com.totoro.xkf.androidclient.util.PreferenceUtils;
import com.totoro.xkf.androidclient.util.StateSelector;
import com.totoro.xkf.androidclient.util.UrlBuilder;
import com.totoro.xkf.androidclient.view.AllHandStateActivity;
import com.totoro.xkf.androidclient.view.HistoryHandStateActivity;
import com.totoro.xkf.androidclient.view.LoginActivity;
import com.totoro.xkf.androidclient.view.StateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.VIBRATOR_SERVICE;

public class StatePresenter extends BasePresenter<StateActivity>
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private LookStateFragment lookStateFragment;
    private StateImageFragment stateImageFragment;
    private Handler mHandler = new Handler(Looper.myLooper());
    private boolean canClose = false;
    private String lastDate = "0000-0000-0000";
    private Vibrator vibrator;

    public StatePresenter(StateActivity mView) {
        super(mView);
        vibrator = (Vibrator) mView.getSystemService(VIBRATOR_SERVICE);
    }

    public FragmentPagerAdapter createAdapter() {
        final List<Fragment> list = new ArrayList<>();
        lookStateFragment = new LookStateFragment();
        lookStateFragment.setPresenter(this);
        stateImageFragment = new StateImageFragment();
        stateImageFragment.setPresenter(this);
        list.add(lookStateFragment);
        list.add(stateImageFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(mView.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        return adapter;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mView.stateDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.hand_state_introduction:
                mView.startActivity(new Intent(mView, AllHandStateActivity.class));
                break;
            case R.id.history_hand_state:
                mView.startActivity(new Intent(mView, HistoryHandStateActivity.class));
                break;
            case R.id.quit:
                mView.startActivity(new Intent(mView, LoginActivity.class));
                PreferenceUtils.saveToken(null);
                mView.finish();
        }
        return true;
    }


    public void startUpdate() {
        mHandler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HttpUtils.updateState(PreferenceUtils.getToken(), callback);
            mHandler.postDelayed(runnable, 5000);
        }
    };

    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String json = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject state = jsonObject.optJSONObject("state");
                String newState = state.optString("state");

                final String date = state.optString("date");
                if (!date.equals(lastDate)) {
                    final String info = StateSelector.getState(newState);
                    final String detail = StateSelector.getHandDetail(newState);

                    final String imageUrl = UrlBuilder.getStateImageUrl(newState);

                    boolean isDanger = state.optBoolean("danger");
                    String checkDanger = "安全";
                    if (isDanger) {
                        checkDanger = "危险";
                        vibrator.vibrate(2000);
                        NotificationUtils.createDangerNotification(mView, "检测到危险手势！！！",
                                "检测到危险手势" + info);
                    }
                    final String danger = checkDanger;

                    String userId = jsonObject.optString("user_id");
                    AppCache.setId(userId);


                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lookStateFragment.updateViews(imageUrl, detail, info, danger, date);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void stopUpdate() {
        mHandler.removeCallbacks(runnable);
    }

    public boolean canClose() {
        if (mView.stateDrawerLayout.isDrawerOpen(Gravity.START)) {
            mView.stateDrawerLayout.closeDrawers();
            return false;
        }
        if (canClose) {
            return true;
        } else {
            Toast.makeText(mView, "两次返回退出应用", Toast.LENGTH_SHORT).show();
            canClose = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canClose = false;
                }
            }, 3000);
            return false;
        }
    }


    @Override
    public void onRefresh() {
        stateImageFragment.stateRefresh.setRefreshing(true);
        HttpUtils.getPicture(PreferenceUtils.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String url = jsonObject.optString("url");
                final String date = jsonObject.optString("date");
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stateImageFragment.updateViews(date, url);
                    }
                });
            }
        });

    }
}
