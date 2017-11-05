package com.nxmup.androidclient.util;

public class UrlBuilder {
    public static String getLoginUrl() {
        return "https://nxmup.com/api/dev/login";
    }

    public static String getTokenUrl() {
        return "https://nxmup.com/api/dev/token";
    }

    public static String getRegisterUrl() {
        return "https://nxmup.com/api/dev/register";
    }

    public static String getUpdateStateUrl() {
        return "https://nxmup.com/api/dev/latest";
    }

    public static String getStateImageUrl(String state) {
        return "https://nxmup.com/static/images/" + state + ".png";
    }

    public static String getHistoryStateUrl() {
        return "https://nxmup.com/api/dev/history";
    }

    public static String getPictureUrl() {
        return "https://nxmup.com/api/dev/latest_picture";
    }
}
