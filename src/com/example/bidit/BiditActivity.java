package com.example.bidit;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BiditActivity extends FragmentActivity implements OnLoginSuccessful {

	private SharedPreferences prefs;

	public BiditActivity() {
		super();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		prefs = Util.getPreferences(this);
		if (prefs.getBoolean("isLoggedIn", false) == false)
		{
			menu.findItem(R.id.action_login).setVisible(true);
			menu.findItem(R.id.action_logout).setVisible(false);
			System.out.println("loggedout");
		}
		
		else
		{
			menu.findItem(R.id.action_login).setVisible(false);
			menu.findItem(R.id.action_logout).setVisible(true);
			System.out.println("loggedin");
		}
		this.invalidateOptionsMenu();
		return super.onCreateOptionsMenu(menu);
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
		case R.id.action_settings:
			//implementation for settings goes here
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}