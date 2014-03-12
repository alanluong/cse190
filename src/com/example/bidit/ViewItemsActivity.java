package com.example.bidit;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ViewItemsActivity extends BiditActivity {

	ItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_items);
		
		adapter = new ItemAdapter(this, R.layout.items_list_item);
		ListView lv = (ListView)findViewById(R.id.item_list);
		lv.setAdapter(adapter);
		new RequestItemsTask().execute();
	}
	
	public class RequestItemsTask extends AsyncTask<Void, Ad, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet request = new HttpGet(Util.AD_API);
			try {
				HttpResponse response = Util.getHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < 3; ++i) {
					JSONObject o = objects.getJSONObject(i);
					
					//TODO set this to current user and only get ads where this user is the seller
					User seller = null;
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					String description = o.getString("description");
					
					Ad ad = new Ad(seller, price, description, description, null);
					ad.setId(o.getInt("id"));
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
	}
	
	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub

	}

}
