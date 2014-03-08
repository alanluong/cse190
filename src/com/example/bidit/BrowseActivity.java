package com.example.bidit;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BrowseActivity extends BiditActivity {

	AdAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		adapter = new AdAdapter(getSupportFragmentManager());
		
		// This is for ListView
		/*ListView lv = (ListView)findViewById(R.id.browseList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) { 
		    	System.out.println("sup");
		    	//if (Config.getCurrentUser() == null) {
				//	new LoginDialogFragment().show(getFragmentManager(),
				//			"login");
				//} else {
					BidDialogFragment bdf = BidDialogFragment.newInstance();
					bdf.show(getFragmentManager(), "BidDialog");
				//}
		    }
		});*/
		
		// Using ViewPager instead
		ViewPager vp = (ViewPager)findViewById(R.id.pager);
		vp.setAdapter(adapter);
		new RequestAdsTask().execute();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class RequestAdsTask extends AsyncTask<Void, Ad, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet request = new HttpGet(Util.AD_API);
			try {
				HttpResponse response = new DefaultHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < objects.length(); ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = null;
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					String description = o.getString("description");
					Drawable image = getResources().getDrawable(R.drawable.ic_launcher);
					Ad ad = new Ad(seller, price, description, description, image);
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
			Log.d(BrowseActivity.class.getName(), "" + adapter.getCount());
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivityForResult(myIntent, 0);
		return true;
	}
}
