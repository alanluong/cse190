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
		itemDescription.setText("this is where the description goes"); //TODO get description from previous screen (ViewItemsActivity)
		
		new RequestBidsTask().execute();
	}
	
	public class RequestBidsTask extends AsyncTask<Void, Bid, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet request = new HttpGet(Util.BID_API);
			try {
				HttpResponse response = Util.getHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < 3; ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = null;
					User buyer = null;
					Ad ad = null;
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					
					Bid bid = new Bid(price, buyer, seller, ad);
					publishProgress(bid);
				}
				Log.d(ViewBidsActivity.class.getName(), content);
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

			Bid it = this.getItem(position);
			TextView bidPrice = (TextView) v.findViewById(R.id.bid_price);
			bidPrice.setText("" + it.getPrice());
			
			Button replyToBid = (Button) v.findViewById(R.id.reply_to_bid);
			replyToBid.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					SendMessageDialogFragment smdf = SendMessageDialogFragment.newInstance();
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
