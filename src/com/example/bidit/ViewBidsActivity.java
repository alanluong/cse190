package com.example.bidit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ViewBidsActivity extends BiditActivity {
	
	BidAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_bids);
		
		adapter = new BidAdapter(this, R.layout.bids_list_item);
		ListView lv = (ListView)findViewById(R.id.bid_list);
		lv.setAdapter(adapter);
		
		TextView itemDescription = (TextView) findViewById(R.id.item_description);
		Bundle b = getIntent().getExtras();
		Ad ad = null;
		if(b != null){
			ad = b.getParcelable("ad");
			itemDescription.setText("[" + ad.getPrice() + "] - " + ad.getDescription());
		}else{
			itemDescription.setText("this is where the description goes");
		}
			
		new RequestBidsTask().execute(ad);
	}
	
	public class RequestBidsTask extends AsyncTask<Ad, Bid, Void> {
		@Override
		protected Void doInBackground(Ad... params) {
			
			String rangeurl = "";
			try {
				rangeurl = "?q=" + URLEncoder.encode("{\"filters\":[{\"name\":\"ad_id\",\"op\":\"eq\",\"val\":"+"\"" + params[0].getId() + "\"}]}", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewBidsActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewBidsActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e1),
							false)
						.build()
				);
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			HttpGet request = new HttpGet(Util.BID_API+rangeurl);
			try {
				HttpResponse response = Util.getHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < objects.length(); ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = new User(o.getString("seller"));
					User buyer = new User(o.getString("bidder"));
					Ad ad = params[0];
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					
					Bid bid = new Bid(price, buyer, seller, ad);
					publishProgress(bid);
				}
				Log.d(ViewBidsActivity.class.getName(), content);
			} catch (ClientProtocolException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewBidsActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewBidsActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (IOException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewBidsActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewBidsActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (JSONException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewBidsActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewBidsActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Bid... bids) {
			adapter.addAll(bids);
			adapter.notifyDataSetChanged();
			Log.d(ViewBidsActivity.class.getName(), "count: " + adapter.getCount());
		}
	}
	
	public class BidAdapter extends ArrayAdapter<Bid> {

		public BidAdapter(Context context, int resource) {
			super(context, resource);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.bids_list_item, null);
			}

			final Bid it = this.getItem(position);
			TextView bidPrice = (TextView) v.findViewById(R.id.bid_price);
			bidPrice.setText("Bidder's Price - $" + it.getPrice());
			
			Button replyToBid = (Button) v.findViewById(R.id.reply_to_bid);
			replyToBid.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					EasyTracker easyTracker = EasyTracker.getInstance(ViewBidsActivity.this);
					easyTracker.send(MapBuilder
							.createEvent("ui_action",
									     "button_press",
									     "reply_button",
									     null)
							.build()
					);
					Message msg = new Message(it.getSeller(), it.getBidder(), null);
					SendMessageDialogFragment smdf = SendMessageDialogFragment.newInstance(msg);
					smdf.show(getFragmentManager(), "login");
				}
				
			});

			
			return v;
		}

	}

	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub

	}

}
