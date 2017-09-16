package com.nxmup.androidclient;
// this class is used to save config variables

class Config {
	final static int HANDLER_MESSAGE_RECEIVE = 0x123;
	// 用于 handler封装与处理数据，主要是与服务器的交互
	final static int HANDLER_MESSAGE_HAND_STATE = 0x124;
	// 用于广播处理，在接收到手指状态变化后对手指状态字符串进行封装与处理
	final static String ACTION_HAND_STATE_CHANGE = "com.nxmup.androidclient.handstatechange";
	// 自定义广播 action 的key
	final static String INTENT_EXTRA_KEY = "com.nxmup.androidclient.intent_extra";
	// 用于封装 Intent 的key

	// 服务器地址与端口
	//	final static String SERVERHOST = "111.231.1.210";		// 部署地址
	final static String SERVERHOST = "10.100.67.235";			// 测试用的本机地址
	final static int SERVERPORT = 21567;						// 端口号

	static String registerationID = null;
	// 每个设备唯一标识 id
	static boolean loginState = false;
	// 登录状态，暂时没有使用
	static int loginUser = 0;
	// 登录用户，暂时没有使用

	// 手指状态，如果有新的手势传递过来直接在这里添加，在 MyReceiver 中添加即可。
	final static String HANDSTATE_DEFAULT;
	final static String HANDSTATE_OPEN;
	final static String HANDSTATE_CLOSE;
	final static String HANDSTATE_LASSO;

	static {
		HANDSTATE_DEFAULT = "Untracked";
		HANDSTATE_OPEN = "Open";
		HANDSTATE_CLOSE = "Close";
		HANDSTATE_LASSO = "Lasso";
	}

	static String state = HANDSTATE_DEFAULT;
}
