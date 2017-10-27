package com.nxmup.androidclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;

import com.nxmup.androidclient.R;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CardView nowState = (CardView) findViewById(R.id.cv_now_state);
        CardView recoverTask = (CardView) findViewById(R.id.cv_recovery_task);
        nowState.setOnClickListener(this);
        recoverTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_now_state:
                startActivity(new Intent(this, NowStateActivity.class));
                finish();
                break;
            case R.id.cv_recovery_task:
                startActivity(new Intent(this, RecoverTaskActivity.class));
                finish();
                break;
        }
    }
}
