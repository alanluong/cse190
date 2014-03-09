package com.example.bidit;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;

import com.example.bidit.SimpleGestureFilter.SimpleGestureListener;

public class BiditActivity extends FragmentActivity {

	public BiditActivity() {
		super();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_login:
			new LoginDialogFragment().show(getFragmentManager(), "login");
			return true;
		case R.id.action_settings:
			//implementation for settings goes here
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}