package com.together.pojo;

public class ItemChat {
	
	public static final int ANSWER = 100;
	public static final int CHAT = 179;
	private int mission_id;
	private int type;
	private String user_id;
	private String message;
	private String name;
	private String url;
	private String user_avatar;
	private boolean correct;
	
	private boolean isSelf;
	
	public ItemChat() {
		
	}
	
	public ItemChat(boolean b) {
		setSelf(b);
	}

	public int getMission_id() {
		return mission_id;
	}

	public void setMission_id(int room_id) {
		this.mission_id = room_id;
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

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	
}
