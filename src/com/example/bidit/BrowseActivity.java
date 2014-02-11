package com.example.bidit;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class BrowseActivity extends Activity implements OnClickListener{
	
	ArrayList<Product> mProducts;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
		
		ListView lv = (ListView) findViewById(R.id.browseList);
		
		mProducts = new ArrayList<Product>();
	    mProducts.add(new Product("foo", "bar"));
	    mProducts.add(new Product("bar", "foo"));
	    
	    ProductsAdapter pAdapter = new ProductsAdapter(
                this, 
                R.layout.products_list_item,
                (Product[]) mProducts.toArray() );

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
		}
		
	}
}
