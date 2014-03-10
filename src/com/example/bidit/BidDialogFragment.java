package com.example.bidit;


import java.io.IOException;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
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

public class BidDialogFragment extends DialogFragment {
	
	private EditText mEditText;

    public BidDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static BidDialogFragment newInstance(String description, String price) {
    	BidDialogFragment frag = new BidDialogFragment();
        Bundle args = new Bundle();
        args.putString("description", description);
        args.putString("price", price);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bid, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_bid);
        
        getDialog().setTitle("[$"+getArguments().getString("price")+"] - " + getArguments().getString("description"));
        
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
				new PutBidTask().execute(view);
			}
        });
        
        return view;
    }
    
    public class PutBidTask extends AsyncTask<View, Void, Void> {
		@Override
		protected Void doInBackground(View... views) {
			View view = views[0];
			String bidPrice = ((EditText) view.findViewById(R.id.txt_your_bid))
					.getText().toString();
			try {
				HttpPost request = new HttpPost(Util.BID_API);
				// TODO: replace with proper data
				JSONObject bids = new JSONObject();
				bids.put("bidder", "testing@test.com");
				bids.put("seller", "testing@test.com");
				bids.put("price", bidPrice);
				bids.put("ad_id", 69);
				Log.d(getActivity().toString(), bids.toString());
				StringEntity entity = new StringEntity(bids.toString());
				request.setEntity(entity);
				request.setHeader("Content-type", "application/json");
				HttpResponse response = Util.getHttpClient().execute(request);

				String content = EntityUtils.toString(response.getEntity());
				Log.d(getActivity().toString(), content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
    }
}
