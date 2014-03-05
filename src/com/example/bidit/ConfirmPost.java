package com.example.bidit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.bidit.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ConfirmPost extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = false;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	String absolutePhotoPath;
	Uri myUri = null;
	EditText description;
	EditText price;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirm_post);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			myUri = Uri.parse(extras.getString("uri"));
			absolutePhotoPath = extras.getString("absolutePath");
		}

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//ImageView contentView = (ImageView) findViewById(R.id.fullscreen_content);
		LinearLayout contentView = (LinearLayout) findViewById (R.id.fullscreen_content);
		LinearLayout wholeView = (LinearLayout) findViewById (R.id.background);
		wholeView.requestFocus();
		
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap( getApplicationContext().getContentResolver(), myUri);
			Drawable d = new BitmapDrawable(getResources(), bitmap);
			//contentView.setImageBitmap(bitmap);
			contentView.setBackground(d);
			//wholeView.setBackground(d);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}
						/*
						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
						*/
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		/*
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
		*/

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		description = (EditText) findViewById(R.id.descriptionText);
		price = (EditText) findViewById(R.id.priceText);
		
		//price.setFocusable(false);
		//description.setFocusable(false);
		
		findViewById(R.id.post_button).setOnClickListener(postItemClickListener);
		findViewById(R.id.priceText).setOnClickListener(priceClickListener);
		findViewById(R.id.descriptionText).setOnClickListener(descriptionClickListener);
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
	
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		//delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	OnClickListener postItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			postItem(absolutePhotoPath);
			
			if(absolutePhotoPath != null)
			{
				File file = new File(absolutePhotoPath);
				if(file.exists())
				{
					file.delete();	
				}
			}
		}
	};
	
	OnClickListener priceClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			price.setFocusableInTouchMode(true);
			price.requestFocus();
		}
	};
	
	OnClickListener descriptionClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			description.setFocusableInTouchMode(true);
			description.requestFocus();
		}
	};
	
	
	private void postItem(String path)
	{		
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		String pathToOurFile = path;
		String urlServer = "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/cell_upload";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;

		try
		{			
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));
				
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			System.out.println("hi");
	
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
	
			// Enable POST method
			connection.setRequestMethod("POST");
	
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; email=\"test@gmail.com\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			
			System.out.println("hi");
			
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			
			System.out.println("hi");
	
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
	
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			System.out.println("hi");
	
			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();
			
			System.out.println(serverResponseMessage);
			
	
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			
		}
		
		catch (Exception ex)
		{
		//Exception handling
		}
		
		finish();
		
		
		
	}

	

}
