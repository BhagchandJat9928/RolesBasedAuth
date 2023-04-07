package com.roles.authenticateroles.cart;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="product")
public class Product {
	
	@Id
	private String id;
	private String name;
	private double price;
	private String photo;

	public Product() {
	}

	public Product(String name, String photo, double price) {
		this.name = name;
		this.price = price;
		this.photo = photo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", photo=" + photo + "]";
	}
	
	

}


