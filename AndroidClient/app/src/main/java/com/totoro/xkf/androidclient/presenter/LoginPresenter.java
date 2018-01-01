package com.totoro.xkf.androidclient.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.app.AppCache;
import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.util.HttpUtils;
import com.totoro.xkf.androidclient.util.PreferenceUtils;
import com.totoro.xkf.androidclient.view.LoginActivity;
import com.totoro.xkf.androidclient.view.StateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginPresenter extends BasePresenter<LoginActivity> implements View.OnClickListener {

    public LoginPresenter(LoginActivity mView) {
        super(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_show_hide_password:
                boolean isSelected = mView.changePasswordType();
                mView.showOrHidePassword(isSelected);
                break;
            case R.id.login_button_login:
                final String account = mView.getAccountText();
                final String password = mView.getPasswordText();
                if (checkEmpty(account, password)) {
                    HttpUtils.login(account, password, new Callback() {
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
                                    if (result.equals("success")) {
                                        AppCache.setId(account);
                                        saveToken(account, password);
                                        mView.startActivity(new Intent(mView, StateActivity.class));
                                        mView.finish();
                                    } else if (result.equals("failed")) {
                                        mView.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mView, "账号密码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
        }
    }

    public boolean checkEmpty(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(mView, "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mView, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveToken(String id, String password) {
        HttpUtils.getToken(id, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String token = jsonObject.optString("token");
                    PreferenceUtils.saveToken(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
