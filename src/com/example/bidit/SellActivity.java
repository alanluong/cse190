package com.example.bidit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SellActivity extends FragmentActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		((Button)findViewById(R.id.camerabutton)).setOnClickListener(this);
		((Button)findViewById(R.id.postbutton)).setOnClickListener(this);
	}
	
	
	private static final int REQUEST_CODE = 10;
	String mCurrentPhotoPath;
	String absolutePhotoPath;
	Uri capturedImageUri = null;
	
	
	@Override
	public void onClick(View v) {
		if(v instanceof Button){
			Button clicked = (Button)v;
			switch(clicked.getId()){
			//case R.id.backbutton2:
			//	Intent intent = new Intent(SellActivity.this, MainActivity.class);
			//	startActivity(intent);
			//	break;
			case R.id.camerabutton:
				EasyTracker easyTracker = EasyTracker.getInstance(this);
				easyTracker.send(MapBuilder
						.createEvent("ui_action",
								     "button_press",
								     "camera_button",
								     null)
						.build()
				);
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
			        	EasyTracker easyTracker1 = EasyTracker.getInstance(SellActivity.this);
						easyTracker1.send(MapBuilder
								.createException(new StandardExceptionParser(SellActivity.this, null)
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
			            startActivityForResult(takePictureIntent,REQUEST_CODE);
			        }
			        
				}
				
				break;
			case R.id.postbutton:
				EasyTracker easyTracker1 = EasyTracker.getInstance(this);
				easyTracker1.send(MapBuilder
						.createEvent("ui_action",
								     "button_press",
								     "post_button",
								     null)
						.build()
				);
				postItem();
				
				finish();
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
	    		  try {
	    			  ImageView imageView= (ImageView) findViewById(R.id.imageView);
	    			  Bitmap bitmap = MediaStore.Images.Media.getBitmap( getApplicationContext().getContentResolver(),  capturedImageUri);
	    			  imageView.setImageBitmap(bitmap);
    			  }
	    		  
	    		  catch (FileNotFoundException e) {
	    			  EasyTracker easyTracker = EasyTracker.getInstance(SellActivity.this);
						easyTracker.send(MapBuilder
								.createException(new StandardExceptionParser(SellActivity.this, null)
									.getDescription(Thread.currentThread().getName(), e),
									false)
								.build()
						);
	    			  e.printStackTrace();
	    		  } 
	    		  
	    		  catch (IOException e) {
	    			  EasyTracker easyTracker = EasyTracker.getInstance(SellActivity.this);
						easyTracker.send(MapBuilder
								.createException(new StandardExceptionParser(SellActivity.this, null)
									.getDescription(Thread.currentThread().getName(), e),
									false)
								.build()
						);
	    			  e.printStackTrace();
	    		  }
	    		  
	    		  System.out.println("In here!!");
	    		  //Bundle extras = data.getExtras();
	    		  //System.out.println("yahoo!!");
	    	      //Bitmap imageBitmap = (Bitmap) extras.get("data");
	    	      System.out.println("espn!!");
	    	      //mImageView.setImageBitmap(imageBitmap);
	    		  break;
	    	  case RESULT_CANCELED:
	    		  break;
	    	  }
	      }
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			//Delete file if back button pressed (Cancel posting item)
			if(absolutePhotoPath != null)
			{
				File file = new File(absolutePhotoPath);
				if(file.exists())
				{
					file.delete();	
				}
			}			
		}
		return super.onKeyDown(keyCode, event);
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
	
	private void postItem()
	{
	    
		
	}
	
	
	

}
