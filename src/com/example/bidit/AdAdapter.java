package com.example.bidit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class AdAdapter extends ArrayAdapter<Ad> {
	private Ad[] ads;
	
	public AdAdapter(Context context, int textViewResourceId, Ad[] products){
		super(context, textViewResourceId, products);
		this.ads = products;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		
		if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.ads_list_item, null);
        }

        Ad it = ads[position];
        if (it != null) {
            ImageView iv = (ImageView) v.findViewById(R.id.list_product_image);
            if (iv != null) {
                iv.setImageDrawable(it.getImage());
            }
        }
		
		return v;
	}
}
