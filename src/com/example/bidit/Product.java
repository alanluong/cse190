package com.example.bidit;

public class Product {
	
	String mTitle;
	String mDescription;
	
	public Product(String s, String d){
		setName(s);
		setDescription(d);
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
}
