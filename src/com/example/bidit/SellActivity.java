package com.example.bidit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SellActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		
		((Button)findViewById(R.id.backbutton2)).setOnClickListener(this);
		((Button)findViewById(R.id.camerabutton)).setOnClickListener(this);
	}
	
	
	private static final int REQUEST_CODE = 10;
	
	
	@Override
	public void onClick(View v) {
		if(v instanceof Button){
			Button clicked = (Button)v;
			switch(clicked.getId()){
			case R.id.backbutton2:
				Intent intent = new Intent(SellActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			case R.id.camerabutton:
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent2,REQUEST_CODE);
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
	    		  break;
	    	  case RESULT_CANCELED:
	    		  break;
	    	  }
	      }
	}

}
