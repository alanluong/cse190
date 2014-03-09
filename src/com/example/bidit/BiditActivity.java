package com.example.bidit;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public abstract class BiditActivity extends FragmentActivity implements OnLoginSuccessful {

	public BiditActivity() {
		super();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_login:
			new LoginDialogFragment().show(getFragmentManager(), "login");
		}
		return super.onOptionsItemSelected(item);
	}


}