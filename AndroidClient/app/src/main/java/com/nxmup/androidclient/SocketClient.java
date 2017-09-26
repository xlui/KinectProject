package com.nxmup.androidclient;

import android.os.Handler;
import android.os.Message;

import com.nxmup.androidclient.constant.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

public class SocketClient {

    public void sendToServer(final String toSend, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在后台新建一个线程向服务器发送数据，并用 handler 封装发送服务器返回的数据
                try {
                    Socket socket = null;
                    BufferedReader reader = null;
                    BufferedWriter writer = null;
                    String response = null;
                    socket = new Socket(Config.SERVERHOST, Config.SERVERPORT);
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(toSend + '\n');
                    writer.flush();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    response = reader.readLine();
                    sendMessageByHandler(response, handler);
                    socket.close();
                    reader.close();
                    writer.close();
                } catch (ConnectException e) {
                    sendMessageByHandler("connectRefuse", handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessageByHandler(final String string, final Handler handler) {
        Message message = handler.obtainMessage();
        message.what = Config.HANDLER_MESSAGE_RECEIVE;
        message.obj = string;
        handler.sendMessage(message);
    }
}

