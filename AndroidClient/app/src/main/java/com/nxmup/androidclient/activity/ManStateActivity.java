package com.nxmup.androidclient.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nxmup.androidclient.R;
import com.nxmup.androidclient.util.HttpUtil;
import com.nxmup.androidclient.util.LogUtil;
import com.nxmup.androidclient.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ManStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_state);
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_man_state_toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.iv_man_state_image);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("当前状态");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        HttpUtil.getPicture(PreferenceUtil.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtil.show(json);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String url = jsonObject.optString("url");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(ManStateActivity.this).load(url).into(imageView);
                    }
                });
            }
        });
    }
}
