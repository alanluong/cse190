package com.example.bidit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Ad> {

	public ItemAdapter(Context context, int resource) {
		super(context, resource);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.items_list_item, null);
		}

		Ad it = this.getItem(position);
		TextView itemDescription = (TextView) v.findViewById(R.id.ad_description);
		itemDescription.setText(it.getDescription());
		
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//TODO put stuff in the bundle to tell ViewBidsActivity which item was clicked
				Intent intent = new Intent(getContext(), ViewBidsActivity.class);
				getContext().startActivity(intent);
			}
		});
		
		return v;
	}

}
