package com.example.bidit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdSlideFragment extends Fragment {
	
	public static Fragment newInstance(int id){
		AdSlideFragment adf = new AdSlideFragment();
		Bundle args = new Bundle();
		args.putInt("adNum", id);
		adf.setArguments(args);
		return adf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ads_list_item, container, false);
		
		int position = getArguments().getInt("adNum");
		Ad it = AdAdapter.getAd(position);
		if (it != null) {
			ImageView iv = (ImageView) rootView.findViewById(R.id.list_product_image);
			if (iv != null) {
				iv.setImageBitmap(it.getImage());
			}
		}
		
		rootView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//if (Config.getCurrentUser() == null) {
				//	new LoginDialogFragment().show(getFragmentManager(),
				//			"login");
				//} else {
					BidDialogFragment bdf = BidDialogFragment.newInstance(getArguments().getInt("adNum"));
					bdf.show(getFragmentManager(), "BidDialog");
				//}
			}
		});
		
		return rootView;
	}
}
