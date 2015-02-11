package com.kpfunusa.bean;

public class Gifts {
	private String company_name;
	private String title;
	private String expiration;
	private String deal_img;
	private String name;
	private String user_img, caption, terms, street_address, city, state, zip_code, voucher, distance,user_id;
	public Gifts() {
		super();
	}
	public Gifts(String company_name, String title, String expiration,
			String deal_img, String name, String user_img, String caption, String terms, String street_address, String city, String state, String zip_code, String voucher, String distance, String user_id) {
		super();
		this.company_name = company_name;
		this.title = title;
		this.expiration = expiration;
		this.deal_img = deal_img;
		this.name = name;
		this.user_img = user_img;
		this.caption = caption;
		this.terms = terms;
		this.street_address = street_address;
		this.city = city;
		this.state = state;
		this.zip_code = zip_code;
		this.voucher = voucher;
		this.distance = distance;
		this.user_id = user_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public String getTitle() {
		return title;
	}
	public String getExpiration() {
		return expiration;
	}
	public String getDealImg() {
		return deal_img;
	}
	public String getName() {
		return name;
	}
	public String getUserImg() {
		return user_img;
	}
	public String getCaption() {
		return caption;
	}
	public String getTerms() {
		return terms;
	}
	public String getAddress() {
		return street_address;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getZipCode() {
		return zip_code;
	}
	public String getVoucher() {
		return voucher;
	}
	public String getDistance() {
		return distance;
	}
	public String getUserId() {
		return user_id;
	}
	
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public void setDeal_img(String deal_img) {
		this.deal_img = deal_img;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUserImg(String user_img) {
		this.user_img = user_img;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public void setAddress(String street_address) {
		this.street_address = street_address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setZipCode(String zip_code) {
		this.zip_code = zip_code;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	
	
}
