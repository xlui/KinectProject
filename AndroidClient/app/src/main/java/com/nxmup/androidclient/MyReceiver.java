package com.nxmup.androidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;


public class MyReceiver extends BroadcastReceiver {
	// 自定义的广播接收器
	final Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (message.what == Config.HANDLER_MESSAGE_RECEIVE) {
					String result = (String)message.obj;
					System.out.println("Get result: " + result);
			}
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
			// 接收 message 推送
			String message = intent.getExtras().getString(JPushInterface.EXTRA_MESSAGE);
			System.out.println("Receive message: " + message);

			if (Config.loginState) {
				// 只有登录的用户才会接收到消息
				Toast.makeText(context, "Message Received: " + message, Toast.LENGTH_SHORT).show();
				onResponse(message, context);
			}
		}
	}

	public void onResponse (final String signal, Context context) {
		SocketClient socketClient = new SocketClient();
		System.out.println("Deal with message(code) received!");
		switch (signal) {
			case "registrationID":
				socketClient.sendToServer("registrationID" + Config.registerationID, handler);
				break;
			case "code:1":
				// 象征手势 打开
				System.out.println("Set state to open");
				sendBroadcast(Config.HANDSTATE_OPEN, context);
				break;
			case "code:2":
				// 象征手势 关闭
				System.out.println("Set state to Close");
				sendBroadcast(Config.HANDSTATE_CLOSE, context);
				break;
			case "code:3":
				// 象征手势 剪刀手
				System.out.println("Set state to Lasso");
				sendBroadcast(Config.HANDSTATE_LASSO, context);
				break;
			// TODO: 2017/9/16 如果 Kinect端有新的手势支持，直接在这里按照上面的格式新建即可
			default:
				break;
		}
	}

	public void sendBroadcast(String handState, Context context) {
		// 发送使用 Action 封装的广播，在 MainActivity 中接收，处理。
		Intent renderIntent = new Intent();
		renderIntent.setAction(Config.ACTION_HAND_STATE_CHANGE);
		renderIntent.putExtra(Config.INTENT_EXTRA_KEY, handState);
		context.sendBroadcast(renderIntent);
	}
}
