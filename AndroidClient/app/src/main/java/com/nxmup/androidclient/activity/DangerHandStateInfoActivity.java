package com.nxmup.androidclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.application.AppCache;

public class DangerHandStateInfoActivity extends AppCompatActivity {
    private ListView lvHandStateInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_hand_state);
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_danger_hand_state_info_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("手势说明");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lvHandStateInfoList = (ListView) findViewById(R.id.lv_danger_hand_state_info_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getDnagerStateInfo());
        lvHandStateInfoList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getDnagerStateInfo());
        lvHandStateInfoList.setAdapter(adapter);
    }

    private String[] getDnagerStateInfo() {
        String[] info = new String[AppCache.getStateService().getDangerHandStateList().size()];
        int position = 0;
        for (String str : AppCache.getStateService().getDangerHandStateList()) {
            String[] message = str.split("@");
            info[position++] = message[0] + "\n" + message[1] + "\n" + message[2];
        }
        return info;
    }
}

