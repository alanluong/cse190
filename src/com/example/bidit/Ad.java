package com.example.bidit;

import android.graphics.drawable.Drawable;

public class Ad {
	
	User seller;
	double price;
	String description;
	Drawable image;
	
	public Ad(String d, Drawable image){
		setDescription(d);
		setImage(image);
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
	
	public String getDescription(){
		return description;
	}
	
	private void setDescription(String s){
		this.description = s;
	}

	public Drawable getImage(){
		return image;
	}
	
	private void setImage(Drawable i){
		this.image = i;
	}
}
