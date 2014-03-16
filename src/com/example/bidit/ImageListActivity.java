package com.example.bidit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageListActivity extends BiditActivity {

	private static final String IMAGE_POSITION = "IMAGE_POSITION";
	private SharedPreferences pref;
	DisplayImageOptions options;
	
	protected ImageLoader imageLoader;
	protected ItemAdapter itmadapter;
	protected ListView listview;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_list);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		//Bundle bundle = getIntent().getExtras();
		itmadapter = new ItemAdapter();
		new RequestAdsTask().execute();

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();

		listview = (ListView) findViewById(android.R.id.list);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//startImagePagerActivity(position);
			}
		});
		
		
	}

	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}

	/*
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		//intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(IMAGE_POSITION, position);
		startActivity(intent);
	}*/

	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private ArrayList<Ad> adapter = new ArrayList<Ad>();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return adapter.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void addAll(Ad... ads){
			for(Ad ad : ads){
				adapter.add(ad);
			}
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(String.format("[$%s] - %s", adapter.get(position).getPrice(), adapter.get(position).getDescription()));

			imageLoader.displayImage(adapter.get(position).getImagePath(), holder.image, options, animateFirstListener);
			
			view.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					EasyTracker easyTracker = EasyTracker.getInstance(ImageListActivity.this);
					easyTracker.send(MapBuilder
							.createEvent("ui_action",
									     "button_press",
									     "item_click",
									     null)
							.build()
					);
					Intent intent = new Intent(ImageListActivity.this, ViewBidsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("description", adapter.get(position).getDescription());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub
		
	}
	
	public class RequestAdsTask extends AsyncTask<Void, Ad, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			
			String rangeurl = "";
			pref = Util.getPreferences(getApplicationContext());
			try {
				String username = pref.getString("Username", "");
				rangeurl = "?q=" + URLEncoder.encode("{\"filters\":[{\"name\":\"email\",\"op\":\"eq\",\"val\":"+"\"" + username + "\"}]}", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			HttpGet request = new HttpGet(Util.AD_API + rangeurl);
			try {
				
				HttpResponse response = Util.getHttpClient().execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < objects.length(); ++i) {
					JSONObject o = objects.getJSONObject(i);
					User seller = new User(o.getString("email"));
					BigDecimal price = new BigDecimal(o.getDouble("price"));
					String description = o.getString("description");
					String imageUrl = (Util.BASE_URL + "/uploads/" + o.getString("id")+".jpg");
					Ad ad = new Ad(seller, price, description, imageUrl);
					ad.setId(o.getInt("id"));
					publishProgress(ad);
				}
			} catch (ClientProtocolException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ImageListActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ImageListActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (IOException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ImageListActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ImageListActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (JSONException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ImageListActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ImageListActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} 
			return null;
		}

		@Override
		protected void onProgressUpdate(Ad... ads) {
			itmadapter.addAll(ads);
			itmadapter.notifyDataSetChanged();
			
		}
		
		@Override
		protected void onPostExecute(Void vd) {
			//wait for ad objects before rendering
			listview.setAdapter(itmadapter);
		}
		
	}
	

	
}