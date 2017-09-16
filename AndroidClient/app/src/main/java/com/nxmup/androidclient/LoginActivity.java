package com.nxmup.androidclient;

import android.content.Intent;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;


public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final EditText editText = (EditText) findViewById(R.id.username);
		Button login = (Button)findViewById(R.id.login);
		Button register = (Button)findViewById(R.id.register);
		// 下面几行代码获取该设备的唯一标识 id，如果不为空（成功获取）就打印出来
		Config.registerationID = JPushInterface.getRegistrationID(this);
		if (Config.registerationID != null) {
			System.out.println("Successfully get registration id!");
			System.out.println("Registration id is: " + Config.registerationID);
		}


		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SocketClient socketClient = new SocketClient();
				String username = editText.getText().toString();

				if (username.isEmpty()) {
					Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				} else {
					try {
						Config.loginUser = Integer.parseInt(username);
						// 限制用户名只能是整数
						socketClient.sendToServer("id:" + username, handler);
						// 发送处理后的登录字符串给服务器，同时发送 handler，用于发送服务器返回消息
					} catch (NumberFormatException e) {
						// 如果用户名包含非数字字符，上面 Integer.parseInt 就不会成功，这里捕捉错误并处理
						Toast.makeText(LoginActivity.this, "用户名中不能包含特殊字符！", Toast.LENGTH_SHORT).show();
						editText.setText("");
					}
				}
			}
		});

		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SocketClient socketClient = new SocketClient();
				String username = editText.getText().toString();

				if (username.isEmpty()) {
					Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				} else {
					try {
						Config.loginUser = Integer.parseInt(username);
						// 限制注册用户名只能是数字
						socketClient.sendToServer("register:" + username +
								":registrationID:" + Config.registerationID, handler);
					} catch (NumberFormatException e) {
						Toast.makeText(LoginActivity.this, "用户名中不能包含特殊字符！", Toast.LENGTH_SHORT).show();
						editText.setText("");
					}
				}
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			// 处理服务器返回的信息，确认登陆成功或者失败
			if (message.what == Config.HANDLER_MESSAGE_RECEIVE)
			{
				String result = (String) message.obj;

				if (result == null) {
					System.out.println("None result");
					Toast.makeText(LoginActivity.this, "服务器无响应！", Toast.LENGTH_SHORT).show();
					return;
				}

				switch (result) {
					case "success":
						// 如果用户名在数据库中，服务器会返回 success 字符串。
						System.out.println("Get result: " + result);
						Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
						// 更新 Config 类中用户状态
						Config.loginState = true;
						// 启动 MainActivity 并结束 LoginActivity
						Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(mainActivity);
						finish();
						break;
					case "already":
						// 服务器维护一个登录列表，如果发送给服务器的用户名已经在登录列表中，服务器返回 already 字符串
						System.out.println("Already login!");
						Toast.makeText(LoginActivity.this, "用户已登录！", Toast.LENGTH_SHORT).show();
						break;
					case "failed":
						// 如果服务器并没有在数据库中查询到用户名，服务器返回 failed
						System.out.println("Get result: " + result);
						Toast.makeText(LoginActivity.this, "用户名未注册到数据库！", Toast.LENGTH_SHORT).show();
						break;
					case "connectRefuse":
						// socket 发送数据到服务器失败，服务器无法连接
						System.out.println("Connect Refused!");
						Toast.makeText(LoginActivity.this, "无法连接服务器，请确认服务器状态！", Toast.LENGTH_SHORT).show();
						break;

					case "already_registered":
						System.out.println("Already registered!");
						Toast.makeText(LoginActivity.this, "用户名已注册！", Toast.LENGTH_SHORT).show();
						break;
					case "register_failed":
						System.out.println("Failed to register!");
						Toast.makeText(LoginActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
						break;
					case "register_success":
						System.out.println("Successfully register!");
						Toast.makeText(LoginActivity.this, "成功注册！", Toast.LENGTH_SHORT).show();
						break;

					default:
						Toast.makeText(LoginActivity.this, "Unknown code!", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

}
