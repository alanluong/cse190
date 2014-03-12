package com.example.bidit;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AdSlideFragment extends Fragment {
	Ad ad = null;

	public AdSlideFragment() {
		super();
	}

	public AdSlideFragment(Ad ad) {
		super();
		this.ad = ad;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public static Fragment newInstance(Ad ad) {
		AdSlideFragment adf = new AdSlideFragment(ad);
		return adf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.ads_list_item, container, false);

		ImageView iv = (ImageView) rootView
				.findViewById(R.id.list_product_image);
		if (iv != null) {
			iv.setImageBitmap((Bitmap) (getArguments().getParcelable("image")));
		}

		rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				User user = ad.getSeller();
				Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT)
						.show();
				BigDecimal price = ad.getPrice();
				Bid bid = new Bid(price, user, user, ad);
				BidDialogFragment bdf = new BidDialogFragment(bid);
				bdf.show(getFragmentManager(), "BidDialog");
			}
		});

		return rootView;
	}
}
