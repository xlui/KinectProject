package com.nxmup.androidclient.util;

public class UrlBuilder {
    public static String getStateUrl() {
        return "http://111.231.1.210/api/dev/login";
    }

    public static String getTokenUrl() {
        return "http://111.231.1.210/api/dev/token";
    }

    public static String getRegisterUrl() {
        return "http://111.231.1.210/api/dev/register";
    }

    public static String getUpdateStateUrl() {
        return "http://111.231.1.210/api/dev/latest";
    }

    public static String getStateImageUrl(String state) {
        return "https://nxmup.com/static/images/" + state + ".png";
    }
}
