package com.nxmup.androidclient;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

class SocketClient {

	void sendToServer(final String toSend, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket socket = null;
					BufferedReader reader = null;
					BufferedWriter writer = null;
					String response = null;

					socket = new Socket(Config.SERVERHOST, Config.SERVERPORT);
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

					System.out.println("Send data to server: " + toSend);
					writer.write(toSend + '\n');
					writer.flush();

					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					response = reader.readLine();
					System.out.println("Get server response: " + response);
					// send message to main thread
					Message msg = handler.obtainMessage();
					msg.what = Config.HANDLER_MESSAGE_RECEIVE;
					msg.obj = response;
					handler.sendMessage(msg);

					socket.close();
					reader.close();
					writer.close();

				} catch (ConnectException e) {
					// e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.what = Config.HANDLER_MESSAGE_RECEIVE;
					msg.obj = "connectRefuse";
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
