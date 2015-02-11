package com.kpfunusa.bean;

public class Restaurant {
	private String id;
	private String company_name;
	private String distance;
	private String address;
	private String img;
	private String img_thumb;
	private String city;
	private String state;
	private String zip_code;
	private String terms;
	private String percent;
	private String description;
	private String reference;
	private String lat;
	private String lng;
	private String website;
	private String reward;
	private String video;
	private String button;
	
	public Restaurant() {
		super();
	}
	
	
	public Restaurant(String id, String company_name, String distance,
			String address, String img, String img_thumb, String city,
			String state, String zip_code, String terms, String percent, String description, String reference, String lat, String lng,
			String website, String reward, String video, String button) {
		super();
		this.id = id;
		this.company_name = company_name;
		this.distance = distance;
		this.address = address;
		this.img = img;
		this.img_thumb = img_thumb;
		this.city = city;
		this.state = state;
		this.zip_code = zip_code;
		this.terms = terms;
		this.percent = percent;
		this.description = description;
		this.reference = reference;
		this.lat=lat;
		this.lng=lng;
		this.website=website;
		this.reward=reward;
		this.video=video;
		this.button=button;
	}


	public String getImg_thumb() {
		return img_thumb;
	}
	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}
	public String getId() {
		return id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public String getDistance() {
		return distance;
	}
	public String getAddress() {
		return address;
	}
	public String getImg() {
		return img;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getZip_code() {
		return zip_code;
	}
	public String getTerms() {
		return terms;
	}
	public String getPercent() {
		return percent;
	}
	public String getDescription() {
		return description;
	}
	public String getReference() {
		return reference;
	}
	public String getLat() {
		return lat;
	}
	public String getLng() {
		return lng;
	}
	public String getWebsite() {
		return website;
	}
	public String getReward() {
		return reward;
	}
	public String getVideo() {
		return video;
	}
	public String getButton() {
		return button;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public void setButton(String button) {
		this.button = button;
	}
		
}
