package com.roles.authenticateroles.user;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.mongodb.lang.Nullable;
import com.roles.authenticateroles.cart.Cart;
import com.roles.authenticateroles.coupon.Coupon;
import com.roles.authenticateroles.orders.Orders;
import com.roles.authenticateroles.subscription.Subscription;

@Document(collection = "user")
public class User implements Serializable {

	private static final long serialVersionUID = -3350141168364333472L;
	@Id
	private String id;
	@Indexed(unique = true)
	private String username;
	@Nullable
	private String name;
	private String Phoneno;
	@Indexed(unique = true)
	private String email;
	@Nullable
	private String image;
	@Nullable
	private Address address;
	@Nullable
	private Address delivery;
	@Nullable
	@DocumentReference
	private Orders orders;
	@Nullable
	@DocumentReference
	private Subscription Subscriptions;
	@Nullable
	private List<String> pastsearch;
	@Nullable
	@DocumentReference
	private List<Coupon> coupons;
	// List<Coupon> couponhistory;

	private String password;

	private String roles;
	private String refreshToken;

	@DBRef()
	private List<Cart> cart;

	public User(String username, String email, String password, String roles, String phoneNo) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.Phoneno = phoneNo;
		this.roles = roles;
		this.name = "";
		this.address = new Address();
		this.cart = List.of();
		this.coupons = List.of();
		this.orders = new Orders();
		this.delivery = new Address();
		this.Subscriptions = new Subscription();
		this.pastsearch = List.of();
	}

	public User() {
		this.name = "";
		this.address = new Address();
		this.cart = List.of();
		this.coupons = List.of();
		this.orders = new Orders();
		this.delivery = new Address();
		this.Subscriptions = new Subscription();
		this.pastsearch = List.of();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneno() {
		return Phoneno;
	}

	public void setPhoneno(String phoneno) {
		Phoneno = phoneno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getDelivery() {
		return delivery;
	}

	public void setDelivery(Address delivery) {
		this.delivery = delivery;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public Subscription getSubscriptions() {
		return Subscriptions;
	}

	public void setSubscriptions(Subscription subscriptions) {
		Subscriptions = subscriptions;
	}

	public List<String> getPastsearch() {
		return pastsearch;
	}

	public void setPastsearch(List<String> pastsearch) {
		this.pastsearch = pastsearch;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", name=" + name + ", Phoneno=" + Phoneno + ", email="
				+ email + ", image=" + image + ", address=" + address + ", delivery=" + delivery + ", orders=" + orders
				+ ", Subscriptions=" + Subscriptions + ", pastsearch=" + pastsearch + ", coupons=" + coupons
				+ ", password=" + password + ", roles=" + roles + ", refreshToken=" + refreshToken + ", cart=" + cart
				+ "]";
	}

}
