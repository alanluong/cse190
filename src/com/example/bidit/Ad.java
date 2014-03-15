package com.example.bidit;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Ad implements Parcelable{
	
	private User seller;
	private BigDecimal price;
	private String description;
	private String imagePath; 
	private String localPath;
	private int id;
	
	public Ad(User seller, BigDecimal price, String description, String imagePath) {
		super();
		this.seller = seller;
		this.price = price;
		this.description = description;
		this.imagePath = imagePath;
		this.localPath = null;
	}
	
	public Ad(Parcel in)
	{
		seller = new User(in.readString());
		price = new BigDecimal(in.readString());
		description = in.readString();
		imagePath = in.readString();
		localPath = in.readString();
		id = in.readInt();
	}
	
	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(seller.getEmail());
		dest.writeString(price.toString());
		dest.writeString(description);
		dest.writeString(imagePath);
		dest.writeString(localPath);
		dest.writeInt(id);		
	}
	
	public static final Parcelable.Creator<Ad> CREATOR = new Parcelable.Creator<Ad>() {
        public Ad createFromParcel(Parcel in) {
            return new Ad(in); 
        }

        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };
}
