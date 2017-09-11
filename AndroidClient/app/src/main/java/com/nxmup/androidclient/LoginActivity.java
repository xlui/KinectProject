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

import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

	private int loginUser = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final EditText editText = (EditText) findViewById(R.id.username);
		Button login = (Button)findViewById(R.id.login);
		Button register = (Button)findViewById(R.id.register);

		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SocketClient socketClient = new SocketClient();

				String username = editText.getText().toString();
				if (username.isEmpty()) {
					Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				} else {
					try {
						loginUser = Integer.parseInt(username);
						// start sending data to server to login
						socketClient.sendToServer("id:" + username, handler);
					} catch (NumberFormatException e) {
						Toast.makeText(LoginActivity.this, "用户名中不能包含特殊字符！", Toast.LENGTH_SHORT).show();
						editText.setText("");
					}
				}
			}
		});

		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这里只是简单的注册，没有另外新建activity
				// todo: 该部分功能尚未实现完全，也未经测试。
				SocketClient socketClient = new SocketClient();

				String username = editText.getText().toString();
				if (username.isEmpty()) {
					Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				} else {
					try {
						loginUser = Integer.parseInt(username);
						// start sending data to server to login
						socketClient.sendToServer("registration" + username, handler);
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
			int HANDLER_MESSAGE_RECEIVE = 0x124;
			if (message.what == HANDLER_MESSAGE_RECEIVE)
			{
				String result = (String) message.obj;

				if (result == null) {
					System.out.println("None result");
					Toast.makeText(LoginActivity.this, "服务器无响应！", Toast.LENGTH_SHORT).show();
					return;
				}

				switch (result) {
					case "success":
						// if username in database, the server will return 'success', now start MainActivity
						System.out.println("Get result: " + result);
						Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
						// modify config variables;
						Config.loginState = true;
						Config.loginUser = loginUser;
						// start main thread
						Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(mainActivity);
						finish();
						break;
					case "already":
						System.out.println("Already login!");
						Toast.makeText(LoginActivity.this, "用户已登录！", Toast.LENGTH_SHORT).show();
					case "failed":
						System.out.println("Get result: " + result);
						Toast.makeText(LoginActivity.this, "用户名未注册到数据库！", Toast.LENGTH_SHORT).show();
						break;
					case "connectRefuse":
						Toast.makeText(LoginActivity.this, "无法连接服务器，请确认服务器状态！", Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(LoginActivity.this, "Unknown code!", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

}
