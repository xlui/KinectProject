package com.nxmup.androidclient.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.util.HttpUtil;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.PreferenceUtil;

import java.io.IOException;

import at.markushi.ui.CircleButton;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Snackbar mSnackbar;
    private CircleButton btnLogin;
    private CircleButton btnRegister;
    private Toolbar tbToolbar;
    private EditText etId;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initViews();
        setListener();
    }

    private void setListener() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tbToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        etId = (EditText) findViewById(R.id.et_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (CircleButton) findViewById(R.id.btn_login);
        btnRegister = (CircleButton) findViewById(R.id.btn_registered);
        mSnackbar = Snackbar.make(tbToolbar, "", Snackbar.LENGTH_SHORT);
        setSupportActionBar(tbToolbar);
        String lastId = PreferenceUtil.getLastId();
        if (!TextUtils.isEmpty(lastId)) {
            //显示上次的账号
            etId.setText(lastId);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String id = etId.getText().toString();
                //点击登录之后存储上一次的账号
                PreferenceUtil.saveLastId(id);
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(id)) {
                    mSnackbar.setText(getString(R.string.emptyIdError)).show();
                    return;
                }
                try {
                    Long.parseLong(id);
                    if (TextUtils.isEmpty(password)) {
                        mSnackbar.setText(getString(R.string.emptyPasswordError)).show();
                    } else {
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
									System.out.println(responseData);
								}
                            }
                        });
                    }
                } catch (Exception e) {
                    mSnackbar.setText(getString(R.string.idInputTypeError)).show();
                }
                break;
            case R.id.btn_registered:
                mSnackbar.setText("还没写。。。。。。").show();
        }
    }
}
