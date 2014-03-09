package com.example.bidit;

import java.math.BigDecimal;

public class Bid {
	private BigDecimal price;
	private User bidder;
	private User seller;
	private Ad ad;
	
	public Bid(BigDecimal p, User b, User s, Ad a){
		setPrice(p);
		setBidder(b);
		setSeller(s);
		setAd(a);
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
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
	public Ad getAd(){
		return ad;
	}
	public void setAd(Ad ad){
		this.ad = ad;
	}
}
