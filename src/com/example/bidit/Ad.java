package com.example.bidit;

import java.math.BigDecimal;

import android.graphics.Bitmap;

public class Ad {
	
	private User seller;
	private BigDecimal price;
	private String description;
	private String imagePath; 
	private String localPath;
	private Bitmap image;
	private int id;
	
	public Ad(User seller, BigDecimal price, String description, String imagePath, Bitmap image) {
		super();
		this.seller = seller;
		this.price = price;
		this.description = description;
		this.imagePath = imagePath;
		this.image = image;
		this.localPath = null;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getLocalPath() {
		return localPath;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public User getSeller() {
		return seller;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}
}
