package com.example.bidit;

import android.graphics.drawable.Drawable;

public class Product {
	
	String mTitle;
	String mDescription;
	
	Drawable mImage;
	
	public Product(String s, String d, Drawable image){
		setName(s);
		setDescription(d);
		
		setImage(image);
	}
	
	public String getName(){
		return mTitle;
	}
	
	private void setName(String s){
		mTitle = s;
	}

	public String getDescription(){
		return mDescription;
	}
	
	private void setDescription(String s){
		mDescription = s;
	}
	
	public Drawable getImage(){
		return mImage;
	}
	
	private void setImage(Drawable i){
		mImage = i;
	}
}
