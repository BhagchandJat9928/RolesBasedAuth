package com.roles.authenticateroles.user;

public class Address {
	
	   private  String address2;
	   private String country;
	   private String State;
	   private String city;
	   private  int pincode;
	   
		public Address() {

		}
	   
	   
	   
	public Address(String address2, String country, String state, String city, int pincode) {
		super();
		this.address2 = address2;
		this.country = country;
		State = state;
		this.city = city;
		this.pincode = pincode;
	}



	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	   
	   
	

}
