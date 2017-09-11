package com.nxmup.androidclient;

// this class is used to save config variables

class Config {
	static final int HANDLER_MESSAGE_RECEIVE=0x124;
	//	final static String SERVERHOST = "111.231.1.210";
	final static String SERVERHOST = "192.168.1.165";		// test host
	final static int SERVERPORT = 21567;

	static String registerationID = null;
	static boolean loginState = false;
	static int loginUser = 0;

	final static String HANDSTATE_OPEN;
	final static String HANDSTATE_CLOSE;
	final static String HANDSTATE_LASSO;

	static {
		HANDSTATE_OPEN = "Open";
		HANDSTATE_CLOSE = "Close";
		HANDSTATE_LASSO = "Lasso";
	}

	static String state = "";
}
