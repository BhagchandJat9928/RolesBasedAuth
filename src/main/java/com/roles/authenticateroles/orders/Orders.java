package com.roles.authenticateroles.orders;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.mongodb.lang.NonNull;
import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.user.Address;

@Document(collection="orders")
public class Orders {
	@Id
	private  String id;
	@NonNull
	@DocumentReference
	private List<Cart> item;
	@NonNull
	 private   String paymentStatus;
	@NonNull
	  private  String paymentOption;
	@NonNull
	 private   Address address;
	 
		public Orders() {
			this.item = List.of();
			this.paymentStatus = "";
			this.paymentOption = "";
			this.address = new Address();
		}
	 
		public Orders(List<Cart> item, String paymentStatus, String paymentOption, Address address) {
		super();
		this.item = item;
		this.paymentStatus = paymentStatus;
		this.paymentOption = paymentOption;
		this.address = address;
	}

	public List<Cart> getItem() {
		return item;
	}

	public void setItem(List<Cart> item) {

		this.item = item;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentOption() {
		return paymentOption;
	}
	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	 
	 
}
