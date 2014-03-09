package com.example.bidit;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdAdapter /*extends ArrayAdapter<Ad>*/ extends FragmentStatePagerAdapter {
	
	private static ArrayList<Ad> mAds;
	
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
		this(fm, null);
	}
	
	public AdAdapter(FragmentManager fm, ArrayList<Ad> def){
		super(fm);
		mAds = new ArrayList<Ad>();
		if(def != null){
			mAds.addAll(def);
		}
	}

	@Override
	public int getCount() {
		return mAds.size();
	}

	@Override
	public Fragment getItem(int position) {
		Ad it = mAds.get(position);
		return AdSlideFragment.newInstance(it.getImage(), it.getDescription(), ""+it.getPrice());
	}
	
	public void addAll(Ad... ads){
		for(Ad ad : ads){
			mAds.add(ad);
		}
	}
	
	public ArrayList<Ad> getAds(){
		return mAds;
	}
	
	public Ad getAd(int pos)
	{
		return mAds.get(pos);
	}
	
	public void addAd(Ad ad)
	{
		mAds.add(ad);
	}
	
}
