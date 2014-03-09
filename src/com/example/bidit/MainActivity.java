package com.example.bidit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import com.example.bidit.SimpleGestureFilter.SimpleGestureListener;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends BiditActivity implements OnClickListener,
		SimpleGestureListener {
	private SimpleGestureFilter detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		detector = new SimpleGestureFilter(this, this);

		((Button) findViewById(R.id.buybutton)).setOnClickListener(this);
		((Button) findViewById(R.id.sellbutton)).setOnClickListener(this);
	}

	private static final int REQUEST_CODE = 10;
	String mCurrentPhotoPath = "";
	String absolutePhotoPath = "";
	Uri capturedImageUri = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof Button) {
			Button clicked = (Button) view;
			switch (clicked.getId()) {
			case R.id.buybutton:
				Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
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
		
		//if(absolutePhotoPath)
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

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_LEFT:
			Intent intent = new Intent(MainActivity.this, MessageActivity.class);
			startActivity(intent);
			break;
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
