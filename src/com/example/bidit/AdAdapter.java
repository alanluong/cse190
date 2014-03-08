package com.example.bidit;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class AdAdapter /*extends ArrayAdapter<Ad>*/ extends FragmentStatePagerAdapter {
	
	static ArrayList<Ad> mAds;
	
	/*public AdAdapter(Context context, int resource) {
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

		Ad it = this.getItem(position);
		if (it != null) {
			ImageView iv = (ImageView) v.findViewById(R.id.list_product_image);
			if (iv != null) {
				iv.setImageDrawable(it.getImage());
			}
		}
		
		return v;
	}*/
	
	public AdAdapter(FragmentManager fm) {
		super(fm);
		mAds = new ArrayList<Ad>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAds.size();
	}

	@Override
	public Fragment getItem(int position) {
		return AdSlideFragment.newInstance(position);
	}
	
	public void addAll(Ad... ads){
		for(Ad ad : ads){
			mAds.add(ad);
		}
	}

	public static Ad getAd(int position) {
		return mAds.get(position);
	}
}
