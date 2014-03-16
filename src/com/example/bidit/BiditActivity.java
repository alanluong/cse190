package com.example.bidit;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BiditActivity extends FragmentActivity implements
		OnLoginSuccessful {

	private SharedPreferences prefs;
	private LoginDialogFragment ldf;
	private IntentFilter intentFilter;
	private BroadcastReceiver broadcastreceiver;

	public BiditActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	    intentFilter = new IntentFilter();
	    intentFilter.addAction("com.package.ACTION_LOGOUT");
	    broadcastreceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }};
	    registerReceiver(broadcastreceiver, intentFilter);
	}
	
	public void onStart(){
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	public void onStop(){
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	public void onPause(){
		super.onPause();
		EasyTracker.getInstance(this).activityStop(this);
		unregisterReceiver(broadcastreceiver);
	}
	
	public void onResume(){
		super.onResume();
		EasyTracker.getInstance(this).activityStop(this);
		registerReceiver(broadcastreceiver, intentFilter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		prefs = Util.getPreferences(this);
		if (prefs.getBoolean("isLoggedIn", false) == false) {
			menu.findItem(R.id.action_login).setVisible(true);
			menu.findItem(R.id.action_logout).setVisible(false);
			menu.findItem(R.id.action_myitems).setVisible(false);
			menu.findItem(R.id.action_mymessages).setVisible(false);
			System.out.println("loggedout");
		}

		else {
			menu.findItem(R.id.action_login).setVisible(false);
			menu.findItem(R.id.action_logout).setVisible(true);
			menu.findItem(R.id.action_myitems).setVisible(true);
			menu.findItem(R.id.action_mymessages).setVisible(true);
			System.out.println("loggedin");
		}

		// this.invalidateOptionsMenu();
		// return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		prefs = Util.getPreferences(this);
		EasyTracker easyTracker = EasyTracker.getInstance(this);
		switch (item.getItemId()) {
		case R.id.action_login:
			easyTracker.send(MapBuilder
					.createEvent("ui_action",
							     "menu_click",
							     "login",
							     null)
					.build()
			);
			ldf = new LoginDialogFragment();
			ldf.show(getFragmentManager(), "login");
			return true;
		case R.id.action_logout:
			easyTracker.send(MapBuilder
					.createEvent("ui_action",
							     "menu_click",
							     "logout",
							     null)
					.build()
			);
			Editor edit = prefs.edit();
			edit.putBoolean("isLoggedIn", false);
			Util.setCurrentUser(null);
			edit.commit();

			Toast.makeText(this, "Logoff Successful", Toast.LENGTH_SHORT)
					.show();
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("com.package.ACTION_LOGOUT");
			sendBroadcast(broadcastIntent);
			
			Intent intent0 = new Intent(this, MainActivity.class);
			startActivity(intent0);
			
			return true;
		case R.id.action_myitems:
			easyTracker.send(MapBuilder
					.createEvent("ui_action",
							     "menu_click",
							     "myitems",
							     null)
					.build()
			);
			Intent intent = new Intent(this, ImageListActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_mymessages:
			easyTracker.send(MapBuilder
					.createEvent("ui_action",
							     "menu_click",
							     "mymessages",
							     null)
					.build()
			);
			Intent intent2 = new Intent(this, ViewMessagesActivity.class);
			startActivity(intent2);
			return true;
		default:
			Intent myIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivityForResult(myIntent, 0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isNetworkOnline() {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);

			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			}

			else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return status;

	}

}
