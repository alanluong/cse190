package com.example.bidit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdSlideFragment extends Fragment {
	
	public static Fragment newInstance(Bitmap img, String description, String price){
		AdSlideFragment adf = new AdSlideFragment();
		Bundle args = new Bundle();
		args.putParcelable("image", img);
		args.putString("description", description);
		args.putString("price", price);
		adf.setArguments(args);
		return adf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ads_list_item, container, false);
		
		ImageView iv = (ImageView) rootView.findViewById(R.id.list_product_image);
		if (iv != null) {
			iv.setImageBitmap((Bitmap)(getArguments().getParcelable("image")));
		}
		
		rootView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//if (Config.getCurrentUser() == null) {
				//	new LoginDialogFragment().show(getFragmentManager(),
				//			"login");
				//} else {
					BidDialogFragment bdf = BidDialogFragment.newInstance(getArguments().getString("description"), getArguments().getString("price"));
					bdf.show(getFragmentManager(), "BidDialog");
				//}
			}
		});
		
		return rootView;
	}
}
