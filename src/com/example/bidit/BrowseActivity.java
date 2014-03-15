package com.example.bidit;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class BrowseActivity extends BiditActivity {

		
	
	AdAdapter adapter;
	ArrayList<Ad> savedAds;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		
		//ActionBar actionBar = getActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		
		if(savedAds == null){
			adapter = new AdAdapter(getSupportFragmentManager(), savedAds);
			new RequestAdsTask().execute();
		}else{
			adapter = new AdAdapter(getSupportFragmentManager());
		}
		
		
		// Using ViewPager instead
		ViewPager vp = (ViewPager)findViewById(R.id.pager);
		vp.setAdapter(adapter);
		
		int position = 0;
		if(savedInstanceState != null){
			position = savedInstanceState.getInt("currentPage");
		}
		Log.d(BrowseActivity.class.getName(), "loaded position: " + position);
		vp.setCurrentItem(position);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		int position = ((ViewPager)(findViewById(R.id.pager))).getCurrentItem();
		Log.d(BrowseActivity.class.getName(), "saved position: " + position);
		savedInstanceState.putInt("currentPage", position);
		savedAds = adapter.getAds();
	}

	public class RequestAdsTask extends AsyncTask<Void, Ad, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet request = new HttpGet(Util.AD_API);
			try {
				HttpResponse response = Util.getHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				Log.e(BrowseActivity.class.getName(), "WHAT IS GOING ON");
				for (int i = 0; i < 3; ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = new User(o.getString("email"));
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					String description = o.getString("description");
					String imageUrl = (Util.BASE_URL + "uploads/" + o.getString("id")+".jpg");
					Log.d("asdf",imageUrl);
					Bitmap image = loadImageFromUrl(imageUrl);
					Ad ad = new Ad(seller, price, description, description);
					ad.setId(o.getInt("id"));
					System.out.println(ad.getSeller().getEmail());
					publishProgress(ad);
				}
				Log.d(BrowseActivity.class.getName(), content);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Ad... ads) {
			adapter.addAll(ads);
			adapter.notifyDataSetChanged();
			Log.d(BrowseActivity.class.getName(), "count: " + adapter.getCount());
		}
		
		private Bitmap loadImageFromUrl(String url)
		{
			
			URL purl;
			Bitmap bmp = null;
			try {
				purl = new URL(url);
				bmp = BitmapFactory.decodeStream(purl.openConnection().getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}
	}

	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub
		
	}
	
	
}
