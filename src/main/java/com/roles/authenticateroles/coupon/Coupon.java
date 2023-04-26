package com.roles.authenticateroles.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="coupons")
public class Coupon {

	@Id
	private String id;
	private String couponcode;
	private String discountamount;
	private String coupondescription;
	private String discountupto;
	private int uselimit;
	
	public Coupon() {}
	
	public Coupon(String couponcode, String discountamount, String coupondescription, String discountupto,
			int uselimit) {
		super();
		this.couponcode = couponcode;
		this.discountamount = discountamount;
		this.coupondescription = coupondescription;
		this.discountupto = discountupto;
		this.uselimit = uselimit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCouponcode() {
		return couponcode;
	}
	public void setCouponcode(String couponcode) {
		this.couponcode = couponcode;
	}
	public String getDiscountamount() {
		return discountamount;
	}
	public void setDiscountamount(String discountamount) {
		this.discountamount = discountamount;
	}
	public String getCoupondescription() {
		return coupondescription;
	}
	public void setCoupondescription(String coupondescription) {
		this.coupondescription = coupondescription;
	}
	public String getDiscountupto() {
		return discountupto;
	}
	public void setDiscountupto(String discountupto) {
		this.discountupto = discountupto;
	}
	public int getUselimit() {
		return uselimit;
	}
	public void setUselimit(int uselimit) {
		this.uselimit = uselimit;
	}
	
	

}
