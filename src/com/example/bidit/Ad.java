package com.example.bidit;

import android.graphics.drawable.Drawable;

public class Ad {
	
	User seller;
	double price;
	String description;
	Drawable image;
	
	public Ad(User seller, double price, String description, Drawable image) {
		super();
		this.seller = seller;
		this.price = price;
		this.description = description;
		this.image = image;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}
}
