package com.totoro.xkf.androidclient.view;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.base.BaseActivity;
import com.totoro.xkf.androidclient.presenter.LoginPresenter;
import com.totoro.xkf.androidclient.util.LogUtils;

public class LoginActivity extends BaseActivity<LoginPresenter> {
    public Toolbar loginToolbar;
    public Button loginButtonLogin;
    public TextInputLayout loginAccountText;
    public TextInputLayout loginPasswordText;
    public TextView loginForgetPassword;
    public TextView loginRegistered;
    public ImageView loginShowHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initViews() {
        loginToolbar = findViewById(R.id.login_toolbar);
        loginButtonLogin = findViewById(R.id.login_button_login);
        loginAccountText = findViewById(R.id.login_account_text);
        loginPasswordText = findViewById(R.id.login_password_text);
        loginForgetPassword = findViewById(R.id.login_forget_password);
        loginShowHidePassword = findViewById(R.id.login_show_hide_password);
        loginRegistered = findViewById(R.id.login_registered);

        loginButtonLogin.setOnClickListener(mPresenter);
        loginShowHidePassword.setOnClickListener(mPresenter);

        setActionBar(loginToolbar, -1, "登录");
    }

    public String getAccountText() {
        Editable editable = loginAccountText.getEditText().getText();
        if (editable == null) {
            return null;
        }
        return editable.toString();
    }

    public String getPasswordText() {
        Editable editable = loginPasswordText.getEditText().getText();
        if (editable == null) {
            return null;
        }
        return editable.toString();
    }

    public boolean changePasswordType() {
        if (loginShowHidePassword.isSelected()) {
            loginShowHidePassword.setSelected(false);
            return false;
        } else {
            loginShowHidePassword.setSelected(true);
            return true;
        }
    }

    public void showOrHidePassword(boolean needShow) {
        EditText editText = loginPasswordText.getEditText();
        if (needShow) {
            loginPasswordText.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        } else {
            loginPasswordText.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        }
    }
}
