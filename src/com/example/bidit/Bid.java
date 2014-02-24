package com.example.bidit;

public class Bid {
	private double price;
	private User bidder;
	private User seller;
	
	public Bid(double p, User b, User s){
		setPrice(p);
		setBidder(b);
		setSeller(s);
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public User getBidder() {
		return bidder;
	}
	public void setBidder(User bidder) {
		this.bidder = bidder;
	}
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
}
