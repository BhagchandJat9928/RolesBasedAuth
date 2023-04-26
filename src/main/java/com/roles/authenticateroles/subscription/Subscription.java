package com.roles.authenticateroles.subscription;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.mongodb.lang.NonNull;
import com.roles.authenticateroles.cart.Product;
import com.roles.authenticateroles.user.Address;
import com.roles.authenticateroles.user.User;

@Document(collection="subscriptions")
public class Subscription {
	@Id
	private String id;

	@DocumentReference(lazy = true)
	User user;

	@DocumentReference
	@NonNull
	private Product product;
	@NonNull
	private  String category;
	@NonNull
	private    String description;
	@NonNull
	private   String paymentStatus;
	@NonNull
	private   String paymentOption;
	@NonNull
	private   String currency;
	@NonNull
	private   String invoice;
	@NonNull
	private   String startdate;
	@NonNull
	private   String enddate;
	@NonNull
	private   String renew;
	@NonNull
	private   String cancel;
	@NonNull
	private   Address address;
	
	public Subscription() {}
	
	public Subscription(String category, String description, String paymentStatus, String paymentOption,
			String currency, String invoice, String startdate, String enddate, String renew, String cancel,
			Address address) {
		super();
		this.category = category;
		this.description = description;
		this.paymentStatus = paymentStatus;
		this.paymentOption = paymentOption;
		this.currency = currency;
		this.invoice = invoice;
		this.startdate = startdate;
		this.enddate = enddate;
		this.renew = renew;
		this.cancel = cancel;
		this.address = address;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRenew() {
		return renew;
	}
	public void setRenew(String renew) {
		this.renew = renew;
	}
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	

	
}
