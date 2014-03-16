package com.example.bidit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.bidit.util.SystemUiHider;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ConfirmPost extends BiditActivity implements OnLoginSuccessful{
	
	String absolutePhotoPath;
	Uri myUri = null;
	EditText descriptionEditText;
	EditText priceEditText;
	Ad ad;
	User user;
	
	SharedPreferences pref;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_confirm_post);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			myUri = Uri.parse(extras.getString("uri"));
			absolutePhotoPath = extras.getString("absolutePath");
			//absolutePhotoPath = extras.getString("uri");
		}
		
		System.out.println(myUri);
		System.out.println(absolutePhotoPath);
		

		ImageView contentView = (ImageView) findViewById(R.id.fullscreen_content1);
		//LinearLayout contentView = (LinearLayout) findViewById (R.id.fullscreen_content);
		//LinearLayout wholeView = (LinearLayout) findViewById (R.id.background);
		//wholeView.requestFocus();
		
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), myUri);
			//Drawable d = new BitmapDrawable(getResources(), bitmap);
			contentView.setImageBitmap(bitmap);
			//contentView.setBackground(d);
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
		/*
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
						
					}
				});
				*/
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
		descriptionEditText = (EditText) findViewById(R.id.descriptionText);
		priceEditText = (EditText) findViewById(R.id.priceText);
		
		//price.setFocusable(false);
		//description.setFocusable(false);
		
		contentView.setOnClickListener(hideKeyboardListener);
		
		findViewById(R.id.post_button).setOnClickListener(postItemClickListener);
		findViewById(R.id.priceText).setOnClickListener(viewClickListener);
		findViewById(R.id.descriptionText).setOnClickListener(viewClickListener);		
		
		((EditText)findViewById(R.id.priceText)).setOnEditorActionListener(donePressListener);
		((EditText)findViewById(R.id.descriptionText)).setOnEditorActionListener(donePressListener); 

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
			//postItem(absolutePhotoPath);
			
			pref = Util.getPreferences(getApplicationContext());
			
			final EditText ad_price= (EditText) findViewById(R.id.priceText);
	    	final EditText ad_description= (EditText) findViewById(R.id.descriptionText);
	    	
	    	if(ad_price.getText().toString().matches("") || ad_description.getText().toString().trim().matches(""))
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPost.this);
                builder.setCancelable(false);
                builder.setTitle("Missing Fields!");
                builder.setMessage("Please check that all the fields have a valid input!");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            	EasyTracker easyTracker = EasyTracker.getInstance(ConfirmPost.this);
                            	easyTracker.send(MapBuilder
                    					.createEvent("ui_action",
                    							     "button_click",
                    							     "missing_fields_ok",
                    							     null)
                    					.build()
                    			);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                alert.getWindow().setLayout(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                
                //WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

    	    	//lp.copyFrom(alert.getWindow().getAttributes());
    	    	//lp.width = 900;
    	    	//lp.height = 1000;
    	    	//lp.x=-170;
    	    	//lp.y=100;
    	    	//alert.getWindow().setAttributes(lp);
	    	}
	    	
	    	else if(!isNetworkOnline())
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPost.this);
                builder.setCancelable(false);
                builder.setTitle("No Network Detected");
                builder.setMessage("Please check your network connection!");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            	EasyTracker easyTracker = EasyTracker.getInstance(ConfirmPost.this);
                            	easyTracker.send(MapBuilder
                    					.createEvent("ui_action",
                    							     "button_click",
                    							     "no_network_ok",
                    							     null)
                    					.build()
                    			);
                                dialog.dismiss();
                            }
                        });
                /*
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });
                 */
                 
                
                AlertDialog alert = builder.create();
                alert.show();
			}
	    	
	    	else 
	    	{
				String username = pref.getString("Username", "");
				
	    		BigDecimal adPrice = new BigDecimal(ad_price.getText().toString());
		    	user = new User(username);
		    	ad = new Ad(user, adPrice, ad_description.getText().toString(), null);
		    	ad.setLocalPath(absolutePhotoPath);
		    	
		    	if(pref.getBoolean("isLoggedIn", false) == false)
		    	{
		    		new LoginDialogFragment().show(getFragmentManager(), "loginDialog");
		    	}
		    	
		    	else
		    	{
			    	
					new AdPost().execute(ad);
					
		    	}
	    	}
			
			
			
		}
	};
	
	OnClickListener hideKeyboardListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	};
	
	OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setFocusableInTouchMode(true);
			v.requestFocus();
		}
	};
	
	OnClickListener descriptionClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			descriptionEditText.setFocusableInTouchMode(true);
			descriptionEditText.requestFocus();
		}
	};
	
	
	OnEditorActionListener donePressListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            	
            	if (v == priceEditText)
            	{
            		descriptionEditText.requestFocus();
            	}
            	
            	else if (v == descriptionEditText)
            	{
            		findViewById(R.id.post_button).requestFocus();
            		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            	}
            	
                return true;
            }
            return false;
        }
    };
    
    
    @Override
	public void onLoginSuccessful() {
    	
    	user.setEmail(pref.getString("Username", ""));
    	System.out.println(user.getEmail());
		new AdPost().execute(ad);
		
	}

	
	class AdPost extends AsyncTask<Ad, Void, Void> {		
	    
	    private ProgressDialog uploadingDialog;

	    @Override
	    protected void onPreExecute() {
	    	uploadingDialog = new ProgressDialog(ConfirmPost.this);
	    	uploadingDialog.setMessage("Posting...");
	    	//uploadingDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    	uploadingDialog.setCancelable(false); 
	    	uploadingDialog.show();
	    	
	    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

	    	lp.copyFrom(uploadingDialog.getWindow().getAttributes());
	    	lp.width = 700;
	    	//lp.height = 150;
	    	//lp.x=-170;
	    	//lp.y=100;
	    	uploadingDialog.getWindow().setAttributes(lp);
	    }
	    
	    @Override
	    protected void onPostExecute(Void string) {
	        try 
	        {	        	
	        	if (uploadingDialog.isShowing()) 
	        	{
	        		uploadingDialog.dismiss();
	        	}
	        	
	        	AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPost.this);
                builder.setCancelable(false);
                builder.setTitle("Post Success!");
                //builder.setMessage("Please check that all the fields have a valid input!");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                
                AlertDialog alert = builder.create();
                alert.show();
                alert.getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);
	        	
	        } 
	        
	        catch(Exception e) {
	        }

	    }
	    

	    protected Void doInBackground(Ad... params) {
	    	Ad ad = params[0];
	    	String filePath = ad.getLocalPath();
	    	String email = ad.getSeller().getEmail();
	    	String description = ad.getDescription();
	    	String price = ad.getPrice().toString();
	    	
	    	
	    	HttpClient client = Util.getHttpClient();  
	    	String postURL = "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/cell_upload";

	        HttpPost post = new HttpPost(postURL); 

	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	        
	        Bitmap bmp = BitmapFactory.decodeFile(filePath);
	        
	        /*
	        Bitmap bmp;
			try {
				bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), myUri);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
	        
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		
    		//Number represents compression amount
    		bmp.compress(CompressFormat.JPEG, 50, bos);
    		InputStream in = new ByteArrayInputStream(bos.toByteArray());
			
    		//ContentBody photoFile = new InputStreamBody(in, "image/jpeg", "filename.jpg");
    		ContentBody photoFile = new InputStreamBody(in, "filename.jpg");
	        		
	        /* example for setting a HttpMultipartMode */
	        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

	        /* example for adding an image part */
	        //FileBody fileBody = new FileBody(new File(filePath)); //image should be a String
	        //builder.addPart("file", fileBody);
	        
	        builder.addPart("file", photoFile); 
	        builder.addTextBody("email", email);
	        builder.addTextBody("description", description);
	        builder.addTextBody("price",  price);

	        post.setEntity(builder.build()); 

	        try 
	        {
	        	HttpResponse response = client.execute(post);
	        	//System.out.println(response);
	        } 
	         
	        catch (ClientProtocolException e) 
	        {
	        	// TODO Auto-generated catch block
				e.printStackTrace();
	        } 
	         
	        catch (IOException e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
	        }
	        
	       
	        
	        try {
	        	bos.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //client.getConnectionManager().shutdown(); 
	        
	        return null;  
	    }
	    
	    

	}


	
	
	/*
	public int uploadFile(String sourceFileUri) {
        
        
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
        
        
        if (!sourceFile.isFile()) {
             
             dialog.dismiss(); 
              
             Log.e("uploadFile", "Source File not exist :"
                                 +uploadFilePath + "" + uploadFileName);
              
             runOnUiThread(new Runnable() {
                 public void run() {
                     messageText.setText("Source File not exist :"
                             +uploadFilePath + "" + uploadFileName);
                 }
             }); 
              
             return 0;
          
        }
        else
        {
        
             try { 
                  
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(upLoadServerUri);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fileName); 
                  
                 dos = new DataOutputStream(conn.getOutputStream());
        
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name="uploaded_file";filename=""
                                           + fileName + """ + lineEnd);
                  
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                    
                  }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("uploadFile", "HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);
                  
                 if(serverResponseCode == 200){
                      
                     runOnUiThread(new Runnable() {
                          public void run() {
                               
                              String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                            +" http://www.androidexample.com/media/uploads/"
                                            +uploadFileName;
                               
                              messageText.setText(msg);
                              Toast.makeText(UploadToServer.this, "File Upload Complete.", 
                                           Toast.LENGTH_SHORT).show();
                          }
                      });                
                 }    
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                   
            } catch (MalformedURLException ex) {
                 
                dialog.dismiss();  
                ex.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToServer.this, "MalformedURLException", 
                                                            Toast.LENGTH_SHORT).show();
                    }
                });
                 
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                 
                dialog.dismiss();  
                e.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToServer.this, "Got Exception : see logcat ", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                                                 + e.getMessage(), e);  
            }
            dialog.dismiss();       
            return serverResponseCode; 
             
         //} // End else block 
       }
	*/
    

	/*
	private void postItem(String path)
	{		 
	    new AdPost().execute(path);
		finish();
	}
	*/
	

	

}
