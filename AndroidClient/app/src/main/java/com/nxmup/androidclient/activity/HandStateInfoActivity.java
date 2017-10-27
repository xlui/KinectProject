package com.nxmup.androidclient.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.util.StateSelector;

public class HandStateInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_state_info);
        Toolbar toolBar = (Toolbar) findViewById(R.id.tb_hand_state_info_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("手势说明");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String[] handStates = new String[StateSelector.handState.length];
        for (int i = 0; i < handStates.length; i++) {
            handStates[i] = StateSelector.handState[i] + "     " + StateSelector.handDetail[i];
        }
        ListView lvHandStateInfoList = (ListView) findViewById(R.id.lv_hand_state_info_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, handStates);
        lvHandStateInfoList.setAdapter(adapter);
    }
}
