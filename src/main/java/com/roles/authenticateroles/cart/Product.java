package com.roles.authenticateroles.cart;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class Product {

	@Id
	private String id;
	private String name;
	private double price;
	private String category;
	private String type;
	private String tag;
	private String date;
	private List<String> image;
	private String currency;
	private String description;

	public Product() {

	}

	public Product(String name, double price, String category, String tag, String type, List<String> image,
			String currency, String description) {
		super();
		this.name = name;
		this.price = price;
		this.category = category;
		this.tag = tag;
		this.type = type;
		this.image = image;
		this.currency = currency;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + ", tag=" + tag
				+ ", date=" + date + ", image=" + image + ", currency=" + currency + ", description=" + description
				+ "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
