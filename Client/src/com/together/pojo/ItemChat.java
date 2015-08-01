package com.together.pojo;

public class ItemChat {
	
	public static final int ANSWER = 100;
	public static final int CHAT = 179;
	public static final int CHAT_ME = 179;
	private int room_id;
	private int type;
	private String user_id;
	private String message;
	private String name;
	private String url;
	
	private boolean isSelf;
	
	public ItemChat(boolean b) {
		setSelf(b);
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
