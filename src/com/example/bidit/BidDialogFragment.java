package com.example.bidit;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BidDialogFragment extends DialogFragment {
	
	private EditText mEditText;
	Bid bid;

    public BidDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    
    public BidDialogFragment(Bid bid) {
    	this.bid = bid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bid, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_bid);
        String title = String.format("[$%s] - %s", bid.getAd().getPrice(), bid.getAd().getDescription());
        getDialog().setTitle(title);
        
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        Button cancelButton = (Button)(view.findViewById(R.id.btn_cancel));
        cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
        });
        Button bidButton = (Button)(view.findViewById(R.id.btn_bid));
        bidButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				new PutBidTask(bid, getActivity()).execute(view);
				dismiss();
			}
        });
        
        return view;
    }

	public class PutBidTask extends AsyncTask<View, Void, Void> {
		private Bid bid;
		Context context;
		
		public PutBidTask(Bid bid, Context context) {
			super();
			this.bid = bid;
			this.context = context;
		}

		@Override
		protected Void doInBackground(View... views) {
			View view = views[0];
			String bidPrice = ((EditText) view.findViewById(R.id.txt_your_bid))
					.getText().toString();
			try {
				SharedPreferences prefs = Util.getPreferences(getActivity());
				HttpPost request = new HttpPost(Util.BID_API);
				JSONObject bids = new JSONObject();
				bids.put("bidder", prefs.getString("Username", ""));
				bids.put("seller", "testing@test.com");
				bids.put("price", bidPrice);
				bids.put("ad_id", bid.getAd().getId());
				Log.d("BidDialog", bids.toString());		
				StringEntity entity = new StringEntity(bids.toString());
				request.setEntity(entity);
				request.setHeader("Content-type", "application/json");
				HttpResponse response = Util.getHttpClient().execute(request);
				String content = EntityUtils.toString(response.getEntity());
				Log.d("BidDialog", content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(context, "Bid placed", Toast.LENGTH_SHORT).show();
		}
		
    }
}
