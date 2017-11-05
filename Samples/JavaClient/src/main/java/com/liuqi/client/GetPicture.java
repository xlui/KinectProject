package com.liuqi.client;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetPicture {
    public static void main(String[] args) {
//        String pictureUrl = "https://nxmup.com/static/images/test.png";
        String pictureUrl = "https://nxmup.com/_uploads/PHOTOS/user_1_2017_11_05_12_34.jpg";
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
                String suffix = connection.getContentType().split("/")[1];

                byte[] bytes = new byte[1024];
                int len = 0;
                File file = new File("t." + suffix);
                OutputStream outputStream = new FileOutputStream(file);
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
