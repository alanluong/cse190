package com.example.bidit;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;

public class Util {
	public static final String BASE_URL = "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/";
	public static final String API = "/api";
	public static final String AD_API = BASE_URL + API + "/Ad";
	public static final String USER_API = BASE_URL + API + "/User";
	public static final String LOGIN = BASE_URL + "/login";
	static private User currentUser;
	public static final String PREFS_NAME = "MyPrefsFile";

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREFS_NAME, 0);
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		Util.currentUser = currentUser;
	}
	
	public static DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	public static void setHttpClient(DefaultHttpClient httpClient) {
		Util.httpClient = httpClient;
	}

	private static DefaultHttpClient httpClient = new DefaultHttpClient();
	
	
	
}
