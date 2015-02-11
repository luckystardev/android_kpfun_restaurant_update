package com.kpfunusa.bean;

public class SearchFriends {
	private String name;
	private String phone;
	
	public SearchFriends() {
		super();
	}
	public SearchFriends(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
