package com.example.bidit;

import java.math.BigDecimal;

import android.graphics.drawable.Drawable;

public class Ad {
	
	private User seller;
	private BigDecimal price;
	private String description;
	private String imagePath; 
	private String localPath;
	private Drawable image;
	
	public Ad(User seller, BigDecimal price, String description, String imagePath, Drawable image) {
		super();
		this.seller = seller;
		this.price = price;
		this.description = description;
		this.imagePath = imagePath;
		this.image = image;
		this.localPath = null;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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
