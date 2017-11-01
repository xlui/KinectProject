package com.liuqi.client;

import okhttp3.*;
import sun.misc.BASE64Encoder;

import java.io.File;

public class SendPicture {
    public static void main(String[] args) {
        String url = "https://nxmup.com/api/dev/upload";

        String username = "1";
        String password = "dev";
        String authorization = new BASE64Encoder().encode((username + ":" + password).getBytes());

        File file = new File("Hello.jpg");
        // 图片在项目根目录下
        assert file.exists();

        SendPicture sendPicture = new SendPicture();
        sendPicture.sendPicture(url, file, authorization);
    }

    private void sendPicture(String url, File file, String authorization) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                // 以下两行为必须
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Basic " + authorization)
                .post(requestBody)
                .build();
        Client client = new Client();
        client.handleResponse(okHttpClient, request);
    }
}
