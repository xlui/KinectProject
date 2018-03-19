package com.totoro.xkf.androidclient.presenter;


import android.support.v4.widget.SwipeRefreshLayout;

import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.util.HttpUtils;
import com.totoro.xkf.androidclient.util.PreferenceUtils;
import com.totoro.xkf.androidclient.view.TrainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TrainPresenter extends BasePresenter<TrainActivity> implements SwipeRefreshLayout.OnRefreshListener {
    public TrainPresenter(TrainActivity mView) {
        super(mView);
    }

    @Override
    public void onRefresh() {
        mView.startRefresh();
        HttpUtils.getTrainTarget(PreferenceUtils.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String date = jsonObject.optString("date");
                    String target = jsonObject.optString("target");
                    mView.setTargetText(date, target, 1);
                    mView.stopRefresh();
                } catch (JSONException e) {

                }
            }
        });
        HttpUtils.getTrainResult(PreferenceUtils.getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String desc = jsonObject.optString("desc");
                    String result = jsonObject.optString("result");
                    mView.setResultText(desc, result, 1);
                    mView.stopRefresh();
                } catch (JSONException e) {
                }
            }
        });
    }
}
