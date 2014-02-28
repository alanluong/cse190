package com.example.bidit;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class BrowseActivity extends Activity implements OnClickListener {

	ArrayList<Ad> mAds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		((Button) findViewById(R.id.backbutton)).setOnClickListener(this);

		ListView lv = (ListView) findViewById(R.id.browseList);

		mAds = new ArrayList<Ad>();
		Resources res = getResources();
		//mAds.add(new Ad("foo", res.getDrawable(R.drawable.ic_launcher)));
		//mAds.add(new Ad("bar", res.getDrawable(R.drawable.ic_launcher)));

		AdAdapter pAdapter = new AdAdapter(this, R.layout.ads_list_item,
				mAds.toArray(new Ad[mAds.size()]));

		lv.setAdapter(pAdapter);
		new RequestAdsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		if (view instanceof Button) {
			Button clicked = (Button) view;
			switch (clicked.getId()) {
			case R.id.backbutton:
				Intent intent = new Intent(BrowseActivity.this,
						MainActivity.class);
				startActivity(intent);
				break;
			}
		} else if (view instanceof ImageView) {
			BidDialogFragment bdf = new BidDialogFragment();
			bdf.show(getFragmentManager(), "BidDialog");
		}
	}

	public class RequestAdsTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet request = new HttpGet(Config.AD_API);
			try {
				HttpResponse response = new DefaultHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < objects.length(); ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = null;
					double price = o.getDouble("price");
					String description = o.getString("description");
					Drawable image = null;
					Ad ad = new Ad(seller, price, description, image);
					mAds.add(ad);
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
	}
}
