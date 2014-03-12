package com.example.bidit;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdAdapter /*extends ArrayAdapter<Ad>*/ extends FragmentStatePagerAdapter {
	
	private static ArrayList<Ad> mAds;
	
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
		return AdSlideFragment.newInstance(it);
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
