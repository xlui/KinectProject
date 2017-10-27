package com.nxmup.androidclient.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Snackbar mSnackbar;
    private Button btnLogin;
    private Button btnRegister;
    private EditText etId;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        bindService();
        initViews();
        setListener();
    }

    private void bindService() {
        Intent intent = new Intent(this, StateService.class);
        bindService(intent, mConnect, BIND_AUTO_CREATE);
    }


    private void setListener() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_registered);
        mSnackbar = Snackbar.make(etPassword, "", Snackbar.LENGTH_SHORT);
        String lastId = PreferenceUtil.getLastId();
        if (!TextUtils.isEmpty(lastId)) {
            //显示上次的账号
            etId.setText(lastId);
        }
    }


    @Override
    public void onClick(View view) {
        final String id = etId.getText().toString();
        final String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(id)) {
            mSnackbar.setText(getString(R.string.emptyIdError)).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mSnackbar.setText(getString(R.string.emptyPasswordError)).show();
            return;
        }
        //点击登录之后存储上一次的账号
        PreferenceUtil.saveLastId(id);
        switch (view.getId()) {
            case R.id.btn_login:
                HttpUtil.login(id, password, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String responseData = responseBody.string();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseData);
                                String result = jsonObject.optString("login");
                                if (result.equals(getString(R.string.success))) {
                                    AppCache.getStateService().setCurrentIdAndPassword(id, password);
                                    startActivity(new Intent(LoginActivity.this, SelectActivity.class));
                                    finish();
                                } else if (result.equals(getString(R.string.failed))) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSnackbar.setText(getString(R.string.errorIdOrPassword)).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                break;
            case R.id.btn_registered:
                HttpUtil.register(id, password, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
        }
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
