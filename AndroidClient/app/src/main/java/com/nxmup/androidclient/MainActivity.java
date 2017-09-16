package com.nxmup.androidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

	private EditText editTextState = null;
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 处理自定义广播接收器响应函数中发出的数据
			if (msg.what == Config.HANDLER_MESSAGE_HAND_STATE) {
				String message = (String)msg.obj;
				editTextState.setText(message);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonGetState = (Button)findViewById(R.id.get_state);
		editTextState = (EditText)findViewById(R.id.state);

		// 自定义一个广播接收器，用于接收 MyReceiver广播接收器中发出的广播。
		// 因为 MyReceiver 中不能直接更新 ui，所以通过 MainActivity 中的 Receiver 使用 handler 更新 ui
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Config.ACTION_HAND_STATE_CHANGE)) {
					String stringMessage = intent.getStringExtra(Config.INTENT_EXTRA_KEY);
					Message message = new Message();
					message.what = Config.HANDLER_MESSAGE_HAND_STATE;
					message.obj = stringMessage;
					handler.sendMessage(message);
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Config.ACTION_HAND_STATE_CHANGE);
		registerReceiver(mReceiver, filter);

		buttonGetState.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextState.setText("Hello World!");
			}
		});
	}
}
