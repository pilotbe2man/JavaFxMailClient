package com.jinyuan.model;

public class EmailConstants {
	
	public static final int LOGIN_STATE_NOT_READY = 0;
	public static final int LOGIN_STATE_FAILED_BY_NETWORK = 1;
	public static final int LOGIN_STATE_FAILED_BY_CREDENTIALS = 2;
	public static final int LOGIN_STATE_SUCCEDED = 3;
	
	//reply or forvard indicator:
	public static final int REPLY_MESSAGE = 4;
	public static final int FORWARD_MESSAGE = 5;
	public static final int STANDALONE_MESSAGE = 6;
	
	//Sending result:
	public static final int MESSAGE_SENT_OK = 7;
	public static final int MESSAGE_SENT_ERROR = 8;

}
