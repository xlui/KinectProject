package com.nxmup.androidclient.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.service.StateService;
import com.nxmup.androidclient.util.HttpUtil;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bindService();
        TextView appName = (TextView) findViewById(R.id.app_name);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(appName, "alpha", 0, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                String token = PreferenceUtil.getToken();
                if (token != null) {
                    HttpUtil.login(token, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                String result = jsonObject.optString("login");
                                if (result.equals(getString(R.string.success))) {
                                    startActivity(new Intent(SplashActivity.this, SelectActivity.class));
                                    finish();
                                } else if (result.equals("failed")) {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    PreferenceUtil.saveToken(null);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        });
        objectAnimator.start();
    }

    private void bindService() {
        Intent intent = new Intent(this, StateService.class);
        bindService(intent, mConnect, BIND_AUTO_CREATE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnect);
    }
}
