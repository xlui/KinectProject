package com.totoro.xkf.androidclient.util;

public class StateSelector {
    public static String[] handState = new String[]{"open_open", "open_close", "open_lasso", "lasso_open", "lasso_close"
            , "lasso_lasso", "close_open", "close_close", "close_lasso"};
    public static String[] handDetail = new String[]{"打开设备", "口渴", "上厕所", "xxxxx", "xxx", "联系亲友", "饥饿",
            "关闭设备", "身体不适"
    };

    public static String getState(String message) {
        for (int i = 0; i < handState.length; i++) {
            if (message.equals(handState[i])) {
                return handDetail[i];
            }
        }
        return null;
    }


    public static String getHandDetail(String message) {
        String[] handState = message.split("_");
        StringBuilder builder = new StringBuilder();
        builder.append("左右手手势为:  ");
        builder.append("左手");
        if (handState[0].equals("open")) {
            builder.append("张开  ");
        } else if (handState[0].equals("lasso")) {
            builder.append("剪刀手  ");
        } else {
            builder.append("握拳  ");
        }
        builder.append("右手");
        if (handState[1].equals("open")) {
            builder.append("张开");
        } else if (handState[1].equals("lasso")) {
            builder.append("剪刀手");
        } else {
            builder.append("握拳");
        }
        return builder.toString();
    }
}
