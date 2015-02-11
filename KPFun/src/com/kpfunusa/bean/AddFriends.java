package com.kpfunusa.bean;

public class AddFriends {
	private String id;
	private String account;
	private int state;
	public AddFriends() {
		super();
	}
	public AddFriends(String id, String account, int state) {
		super();
		this.id = id;
		this.account = account;
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public String getAccount() {
		return account;
	}
	public int getState() {
		return state;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	
}
