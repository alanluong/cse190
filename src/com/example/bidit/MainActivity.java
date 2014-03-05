package com.example.bidit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener, SimpleGestureListener{
	private SimpleGestureFilter detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		detector = new SimpleGestureFilter(this,this);
		
		((Button)findViewById(R.id.buybutton)).setOnClickListener(this);
		((Button)findViewById(R.id.sellbutton)).setOnClickListener(this);
	}
	
	
	
	private static final int REQUEST_CODE = 10;
	String mCurrentPhotoPath;
	String absolutePhotoPath;
	Uri capturedImageUri = null;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	@Override
	public void onClick(View view) {
		System.out.println("something clicked");
		if(view instanceof Button){
			Button clicked = (Button)view;
			System.out.println("button clicked");
			switch(clicked.getId()){
			case R.id.buybutton:
				System.out.println("buy button clicked");
				Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
				startActivity(intent);
				break;
			/*
			case R.id.sellbutton:
				Intent intent1 = new Intent(MainActivity.this, SellActivity.class);
				startActivity(intent1);
				break;
			*/
			case R.id.sellbutton:
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					
					if(absolutePhotoPath != null)
					{
						File file = new File(absolutePhotoPath);
						if(file.exists())
						{
							boolean deleted = file.delete();	
							System.out.println(deleted);
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
			            startActivityForResult(takePictureIntent,REQUEST_CODE);
			        }
			        
				}
				break;
			}
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	      // TODO Auto-generated method stub
	      //super.onActivityResult(requestCode, resultCode, data);
	      if(requestCode == REQUEST_CODE) {
	    	  switch(resultCode){
	    	  case RESULT_OK:
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
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "IMG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    absolutePhotoPath = image.getAbsolutePath();
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    
	    return image;
	}
	
	
}
	

