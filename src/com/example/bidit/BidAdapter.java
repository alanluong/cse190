package com.example.bidit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
			v = vi.inflate(R.layout.ads_list_item, null);
		}

		Bid it = this.getItem(position);
		//set stuff in the view
		
		return v;
	}

}
