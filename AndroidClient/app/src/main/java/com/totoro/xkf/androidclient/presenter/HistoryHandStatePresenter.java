package com.totoro.xkf.androidclient.presenter;

import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.util.HttpUtils;
import com.totoro.xkf.androidclient.util.PreferenceUtils;
import com.totoro.xkf.androidclient.util.StateSelector;
import com.totoro.xkf.androidclient.view.HistoryHandStateActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryHandStatePresenter extends BasePresenter<HistoryHandStateActivity> {
    public HistoryHandStatePresenter(HistoryHandStateActivity mView) {
        super(mView);
    }

    public void updateHistoryData() {
        HttpUtils.getHistoryState(PreferenceUtils.getToken(), new Callback() {
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
                        String stateInfo = StateSelector.getState(state);
                        boolean isDanger = jsonObject.optBoolean("danger");
                        String danger = "安全";
                        if (isDanger) {
                            danger = "危险";
                        }
                        String date = jsonObject.optString("date");
                        String info = stateInfo + "\n" + danger + "\n" + date;
                        history.add(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!history.isEmpty()) {
                    String[] historyData = history.toArray(new String[history.size()]);
                    mView.updateListView(historyData);
                }
            }
        });
    }
}
