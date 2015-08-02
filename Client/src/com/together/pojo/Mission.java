package com.together.pojo;

import java.util.List;

public class Mission {
	
	public static final int SPEAK = 23;
	
	private String name;
	private int id;
	private int type;
	private List<String> words;
	private String ownerId;
	private String ownerName;
	private int minUsers;
	private int numUsers;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String owner) {
		this.ownerId = owner;
	}

	public int getMinUsers() {
		return minUsers;
	}

	public void setMinUsers(int minUsers) {
		this.minUsers = minUsers;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}

}
