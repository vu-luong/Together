package com.together.constants;

public class Events {
	public static final String CONNECTED = "connect";
	public static final String REGISTER = "register";
	public static final String REGISTER_SUCCEED = "register success";
	public static final String NEW_MISSION = "new mission";
	public static final String NEW_MISSION_SUCCEED = "new mission success";
	public static final String JOIN_MISSION = "join misison";
	public static final String JOIN_MISSION_SUCCEED = "join mission success";
	public static final String LEAVE_MISSION = "leave mission";
	public static final String SERVICE_FAILURED = "service failure";
	public static final String CHAT = "chat";
	public static final String DISCONNECT = "disconnect";
	public static final String LOGIN = "login";
	public static final String LOGIN_SUCCEED = "login success";
	public static final String GET_MY_MISSION = "get mission";
	public static final String GET_MY_MISSION_SUCCEED = "get mission success";
	public static final String RELOGIN = "relogin";
	
	public static final String[] EVENTS = new String[] {
		CONNECTED, REGISTER, REGISTER_SUCCEED, NEW_MISSION, NEW_MISSION_SUCCEED, JOIN_MISSION,
		JOIN_MISSION_SUCCEED, LEAVE_MISSION, SERVICE_FAILURED, CHAT, DISCONNECT, LOGIN, LOGIN_SUCCEED,
		GET_MY_MISSION, GET_MY_MISSION_SUCCEED, RELOGIN
	};
	
	
}
