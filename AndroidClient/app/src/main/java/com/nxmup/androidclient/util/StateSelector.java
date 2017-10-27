package com.nxmup.androidclient.util;


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

}
