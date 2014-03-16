package com.example.bidit;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public abstract class BiditActivity extends FragmentActivity implements
		OnLoginSuccessful {

	private SharedPreferences prefs;
	private LoginDialogFragment ldf;

	public BiditActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void onStart(){
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	public void onStop(){
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
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
