package com.example.bidit;

import com.example.bidit.SimpleGestureFilter.SimpleGestureListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
			case R.id.sellbutton:
				Intent intent1 = new Intent(MainActivity.this, SellActivity.class);
				startActivity(intent1);
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
}
