package com.liuqi.client;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetPicture {
    public static void main(String[] args) {
        String pictureUrl = "https://nxmup.com/static/images/test.png";
        URL url = null;
        try {
            url = new URL(pictureUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            URLConnection connection = null;
            if (url != null) {
                connection = url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                InputStream inputStream = connection.getInputStream();

                byte[] bytes = new byte[1024];
                int len = 0;
                File file = new File(".");
                OutputStream outputStream = new FileOutputStream(file.getPath() + "\\t.png");
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }

                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
