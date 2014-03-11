package com.example.bidit;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BiditActivity extends FragmentActivity implements OnLoginSuccessful {

	private SharedPreferences prefs;

	public BiditActivity() {
		super();
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		prefs = Util.getPreferences(this);
		if (prefs.getBoolean("isLoggedIn", false) == false)
		{
			menu.findItem(R.id.action_login).setVisible(true);
			menu.findItem(R.id.action_logout).setVisible(false);
			menu.findItem(R.id.action_myitems).setVisible(false);
			menu.findItem(R.id.action_mymessages).setVisible(false);
			System.out.println("loggedout");
		}
		
		else
		{
			menu.findItem(R.id.action_login).setVisible(false);
			menu.findItem(R.id.action_logout).setVisible(true);
			menu.findItem(R.id.action_myitems).setVisible(true);
			menu.findItem(R.id.action_mymessages).setVisible(true);
			System.out.println("loggedin");
		}
		
		//this.invalidateOptionsMenu();
		//return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		prefs = Util.getPreferences(this);
		switch (item.getItemId()) {
		case R.id.action_login:
			new LoginDialogFragment().show(getFragmentManager(), "login");
			return true;
		case R.id.action_logout:
			Editor edit = prefs.edit();
			edit.putBoolean("isLoggedIn", false);
			edit.commit();
			return true;
		case R.id.action_myitems:
			Intent intent = new Intent(this, ViewItemsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_mymessages:
			Intent intent2 = new Intent(this, ViewMessagesActivity.class);
			startActivity(intent2);
			return true;
		default:
			Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
			startActivityForResult(myIntent, 0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}