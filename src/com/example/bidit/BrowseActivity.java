package com.example.bidit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class BrowseActivity extends Activity implements OnClickListener{
	
	ArrayList<Ad> mAds;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		((Button)findViewById(R.id.backbutton)).setOnClickListener(this);
		
		ListView lv = (ListView) findViewById(R.id.browseList);
		
		mAds = new ArrayList<Ad>();
		Resources res = getResources();
	    mAds.add(new Ad("foo", "bar", res.getDrawable(R.drawable.ic_launcher)));
	    mAds.add(new Ad("bar", "foo", res.getDrawable(R.drawable.ic_launcher)));
	    
	    AdAdapter pAdapter = new AdAdapter(
                this, 
                R.layout.ads_list_item,
                mAds.toArray(new Ad[mAds.size()]) );

        lv.setAdapter(pAdapter); 
	    
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
			case R.id.backbutton:
				Intent intent = new Intent(BrowseActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			}
		}else if(view instanceof ImageView){
			
		}
		
	}
}
