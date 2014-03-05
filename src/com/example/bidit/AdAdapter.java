package com.example.bidit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class AdAdapter extends ArrayAdapter<Ad> {
	private Context activityContext;
	
	public AdAdapter(Context context, int resource) {
		super(context, resource);
		activityContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.ads_list_item, null);
		}

		Ad it = this.getItem(position);
		if (it != null) {
			ImageView iv = (ImageView) v.findViewById(R.id.list_product_image);
			if (iv != null) {
				iv.setImageDrawable(it.getImage());
				iv.setOnClickListener(new OnClickListener(){
					public void onClick(View view){
						BidDialogFragment bdf = new BidDialogFragment();
						bdf.show(((BrowseActivity)activityContext).getFragmentManager(), "BidDialog");
					}
				});
			}
		}

		return v;
	}
}
