package com.together.constants;

public class Constants {
	
	// Preferences
	public static final String MY_PREFS_NAME = "my_preferences";
	public static final String ID_KEY = "id_key";
	public static final String USERNAME_KEY = "usename_key";
	
	// Key to find event
	public static final String EVEN_BUNDLE_TAG = "event";
	
	// Web socket events
	public static final String SOCKET_SERVER = "http://10.0.239.115:9595";
	public static final String EVENT_REGISTER = "register";
	public static final String EVENT_REGISTER_SUCCEED = "register success";
	public static final String EVEN_REGISTER_FAILURE = "register failure";
	public static final String[] EVENTS_LIST = new String[] {
		SOCKET_SERVER,
		EVEN_REGISTER_FAILURE,
		EVENT_REGISTER,
		EVEN_REGISTER_FAILURE
	};
	public static final String AUDIO_ENDPOINT = "http://192.168.32.107:8080";;
	
	
	
}
