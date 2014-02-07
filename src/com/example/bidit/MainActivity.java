package com.example.bidit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

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
				break;
			}
		}
		
	}

}
