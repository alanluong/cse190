package com.example.bidit;

import com.example.bidit.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;


public class MessageActivity extends Activity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);	
		
		detector = new SimpleGestureFilter(this,this);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			Intent intent = new Intent(MessageActivity.this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}
}
