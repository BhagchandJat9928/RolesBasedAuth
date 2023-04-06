package com.roles.authenticateroles.user;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="user")
public class User implements Serializable{
	
	private static final long serialVersionUID = -3350141168364333472L;

	@Indexed(unique=true)
	String username;
	
	String password;
	
	String roles;
	String refreshToken;
	
	
	
	public User(String username, String password, String roles) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
	
	}
	public User() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", roles=" + roles + ", refreshToken="
				+ refreshToken + "]";
	}
	
	

}
