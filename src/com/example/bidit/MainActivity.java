package com.example.bidit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends BiditActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.buybutton)).setOnClickListener(this);
		((Button) findViewById(R.id.sellbutton)).setOnClickListener(this);
	}

	private static final int REQUEST_CODE = 10;
	String mCurrentPhotoPath = "";
	String absolutePhotoPath = "";
	Uri capturedImageUri = null;

	@Override
	public void onClick(View view) {
		if (view instanceof Button) {
			Button clicked = (Button) view;
			switch (clicked.getId()) {
			case R.id.buybutton:
				EasyTracker easyTracker = EasyTracker.getInstance(this);
				easyTracker.send(MapBuilder
						.createEvent("ui_action",
								     "button_press",
								     "buy_button",
								     null)
						.build()
				);
				//Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
				Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
				startActivity(intent);
/*	
		if(view instanceof Button){
			Button clicked = (Button)view;
			switch(clicked.getId()){
			case R.id.buybutton:
				//Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
				//startActivity(intent);
				//FragmentManager fm = getFragmentManager();
				//LoginDialogFragment myInstance = new LoginDialogFragment();
				//myInstance.show(fm, "some_tag");
				//ProgressDialog.Builder builder = new ProgressDialog.Builder(MainActivity.this);

				ProgressDialog uploadingDialog;
				uploadingDialog = new ProgressDialog(MainActivity.this);
				uploadingDialog.setMessage("Posting...");
		    	uploadingDialog.getWindow().setLayout(300, 300);
				uploadingDialog.setCancelable(false); 
				

		    	uploadingDialog.show();
		    	*/
				break;
			case R.id.sellbutton:
				EasyTracker easyTracker1 = EasyTracker.getInstance(this);
				easyTracker1.send(MapBuilder
						.createEvent("ui_action",
								     "button_press",
								     "sell_button",
								     null)
						.build()
				);
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

					if (absolutePhotoPath != null) {
						File file = new File(absolutePhotoPath);
						if (file.exists()) {
							file.delete();
						}
					}

					File photoFile = null;

					try {
						photoFile = createImageFile();
					}

					catch (IOException ex) {
						EasyTracker easyTracker2 = EasyTracker.getInstance(MainActivity.this);
						easyTracker2.send(MapBuilder
								.createException(new StandardExceptionParser(MainActivity.this, null)
									.getDescription(Thread.currentThread().getName(), ex),
									false)
								.build()
						);
						// Error occurred while creating the File
					}

					// Continue only if the File was successfully created
					if (photoFile != null) {
						capturedImageUri = Uri.fromFile(photoFile);
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
						startActivityForResult(takePictureIntent, REQUEST_CODE);
					}

				}
				break;
			}
		}

	}
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString("pathString", absolutePhotoPath);
		
		if(capturedImageUri != null)
		{
			outState.putString("uriString", capturedImageUri.toString());
		}
		
		else
		{
			outState.putString("uriString", "");
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
		capturedImageUri = Uri.parse(savedInstanceState.getString("uriString"));
		absolutePhotoPath = savedInstanceState.getString("pathString");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				/*
				File f = new File(absolutePhotoPath);
				
				Bitmap originalBitmap = BitmapFactory.decodeFile(absolutePhotoPath);
				
				int newHeight = (int) (originalBitmap.getHeight()*.95);
				int newWidth = (int) (originalBitmap.getWidth()*.95);
				
				/*
				if((originalBitmap.getHeight() > originalBitmap.getWidth()) && originalBitmap.getHeight() > 1920)
				{
					newHeight = 1920;
					newWidth = 1080;
					originalBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
				}
				
				else
				if((originalBitmap.getHeight() < originalBitmap.getWidth()) && originalBitmap.getHeight() > 1080)
				{
					newHeight = 1080;
					newWidth = 1920;
					originalBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
				}
				
				
				originalBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
				
	    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		
	    		originalBitmap.compress(CompressFormat.JPEG, 50, bos);
	    		
	    		byte[] bitmapdata = bos.toByteArray();

	    		//write the bytes in file
	    		
	    		try {
	    			FileOutputStream fos = new FileOutputStream(f);
					fos.write(bitmapdata);
					fos.flush();
		    		fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		*/
	    		
				
				Intent confirmPost = new Intent(MainActivity.this, ConfirmPost.class);
				confirmPost.putExtra("absolutePath", absolutePhotoPath);
				confirmPost.putExtra("uri", capturedImageUri.toString());
				startActivity(confirmPost);
				break;
			case RESULT_CANCELED:
				break;
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "IMG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		absolutePhotoPath = image.getAbsolutePath();
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();

		return image;
	}

	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub
		
	}

}
