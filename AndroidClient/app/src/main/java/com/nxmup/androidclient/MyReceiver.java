package com.nxmup.androidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;


public class MyReceiver extends BroadcastReceiver {

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
				case Config.HANDLER_MESSAGE_RECEIVE:
					String result = (String)message.obj;
					System.out.println("Get result: " + result);
					break;
			}
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
			if (Config.registerationID == null) {
				Config.registerationID = JPushInterface.getRegistrationID(context);
				System.out.println("successfully set registrationID, RegistrationID is: " + Config.registerationID);
			}

			Bundle bundle = intent.getExtras();
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			System.out.println("Receive message: " + message);

			// TODO: 2017/8/18 Add user detect, only login user can receive message. Only special user can receive special message
			if (Config.loginState) {
				Toast.makeText(context, "Message Received: " + message, Toast.LENGTH_SHORT).show();
				onResponse(message);
			}
		}
	}

	public void onResponse (final String signal) {
		SocketClient socketClient = new SocketClient();
		System.out.println("Deal with message(code) received!");
		switch (signal) {
			// TODO: 2017/8/17 If server want get registration id from android device, send 'registrationID' to client, and client will get and send registrationID to server
			case "registrationID":
				socketClient.sendToServer("registrationID" + Config.registerationID, handler);
				break;
			// TODO: 2017/8/17 Detect android client's login status
//			case "login":
//				if (Config.state) {
//					socketClient.sendToServer();
//				}
			case "code:1":
				// symbols hand open
				System.out.println("Set state to open");
				Config.state = Config.HANDSTATE_OPEN;
				break;
			case "code:2":
				// symbols hand close
				System.out.println("Set state to Close");
				Config.state = Config.HANDSTATE_CLOSE;
				break;
			case "code:3":
				// symbols hand lasso
				System.out.println("Set state to Lasso");
				Config.state = Config.HANDSTATE_LASSO;
				break;
		}
	}
}
