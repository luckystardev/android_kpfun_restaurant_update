package com.kpfunusa.bean;

public class Rewards {
	private String company_name;
	private String percent;
	private String expiration;
	private String address;
	private String img;
	private String city;
	private String state;
	private String zip_code;
	private String voucher;
	private String name;
	private String terms;
	
	
	public Rewards() {
		super();
	}

	public Rewards(String company_name, String percent, String expiration,
			String address, String img, String city, String state,
			String zip_code, String voucher, String name, String terms) {
		super();
		this.company_name = company_name;
		this.percent = percent;
		this.expiration = expiration;
		this.address = address;
		this.img = img;
		this.city = city;
		this.state = state;
		this.zip_code = zip_code;
		this.voucher = voucher;
		this.name=name;
		this.terms=terms;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public String getCompany_name() {
		return company_name;
	}

	public String getPercent() {
		return percent;
	}

	public String getExpiration() {
		return expiration;
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
	public String getName() {
		return name;
	}
	public String getTerms() {
		return terms;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
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
	public void setName(String name) {
		this.name = name;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}

}
