package com.nxmup.androidclient.constant;


public class Config {
    public final static int HANDLER_MESSAGE_RECEIVE = 0x123;
    // 用于 handler封装与处理数据，主要是与服务器的交互
    public final static int HANDLER_MESSAGE_HAND_STATE = 0x124;
    // 用于广播处理，在接收到手指状态变化后对手指状态字符串进行封装与处理
    public final static String ACTION_HAND_STATE_CHANGE = "com.nxmup.androidclient.handstatechange";
    // 自定义广播 action 的key
    public final static String INTENT_EXTRA_KEY = "com.nxmup.androidclient.intent_extra";
    // 用于封装 Intent 的key

    // 服务器地址与端口
<<<<<<< HEAD
    //	final static String SERVERHOST = "111.231.1.210";		// 部署地址
//    public final static String SERVERHOST = "10.100.67.27";            // 测试用的本机地址
//    public final static int SERVERPORT = 21567;                        // 端口号

=======
//    public final static String SERVERHOST = "111.231.1.210";		// 部署地址
    public final static String SERVERHOST = "10.100.66.107";            // 测试用的本机地址
    public final static int SERVERPORT = 21567;                        // 端口号
>>>>>>> 0d7eaa18059d1d4ef0d0337c1cce367ee74302fe

    public static String registerationID = null;
    // 每个设备唯一标识 id
    public static boolean loginState = false;
    // 登录状态，暂时没有使用
    public static int loginUser = 0;
    // 登录用户，暂时没有使用

    // 手指状态，如果有新的手势传递过来直接在这里添加，在 MyReceiver 中添加即可。
    public final static String HANDSTATE_DEFAULT = "Untracked";
    public final static String HANDSTATE_OPEN = "Open";
    public final static String HANDSTATE_CLOSE = "Close";
    public final static String HANDSTATE_LASSO = "Lasso";

    public static String state = HANDSTATE_DEFAULT;
}

