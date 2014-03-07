package com.example.bidit;

public class Config {
	public static final String BASE_URL = "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/";
	public static final String API = "/api";
	public static final String AD_API = BASE_URL + API + "/Ad";
	public static final String USER_API = BASE_URL + API + "/User";
	static private User currentUser;

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		Config.currentUser = currentUser;
	}
}
