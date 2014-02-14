package com.example.bidit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button sellButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sellButton = (Button)findViewById(R.id.sellbutton);
		sellButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(MainActivity.this, SellActivity.class);
				startActivity(intent2);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
/*
	@Override
	public void onClick(View view) {
		if(view instanceof Button){
			Button clicked = (Button)view;
			switch(clicked.getId()){
			case R.id.buybutton:
				Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
				startActivity(intent);
				break;
			case R.id.sellbutton:
				Intent intent2 = new Intent(MainActivity.this, SellActivity.class);
				startActivity(intent2);
				break;
			}
		}
		
	}
*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
