package com.example.bidit;

import java.io.IOException;
import java.math.BigDecimal;
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

	AdAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		((Button) findViewById(R.id.backbutton)).setOnClickListener(this);

		ListView lv = (ListView) findViewById(R.id.browseList);

		//Resources res = getResources();
		//mAds.add(new Ad("foo", res.getDrawable(R.drawable.ic_launcher)));
		//mAds.add(new Ad("bar", res.getDrawable(R.drawable.ic_launcher)));

		adapter = new AdAdapter(this, R.layout.ads_list_item);
		lv.setAdapter(adapter);
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
		}
	}

	public class RequestAdsTask extends AsyncTask<Void, Ad, Void> {
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
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					String description = o.getString("description");
					Drawable image = getResources().getDrawable(R.drawable.ic_launcher);
					Ad ad = new Ad(seller, price, description, image);
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
}
