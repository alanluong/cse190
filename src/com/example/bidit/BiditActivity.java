package com.example.bidit;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BiditActivity extends FragmentActivity implements OnLoginSuccessful {

	public BiditActivity() {
		super();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//if(loggedIn){
			MenuItem loginItem = menu.getItem(0);
			loginItem.setTitle("Logout");
			menu.add(Menu.NONE, R.id.action_mybids, Menu.NONE, "My Bids");
			menu.add(Menu.NONE, R.id.action_mymessages, Menu.NONE, "My Messages");
		//}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_login:
				if(item.getTitle().equals("Login")){
					new LoginDialogFragment().show(getFragmentManager(), "login");
				}else{
					//logout
				}
				return true;
			case R.id.action_mybids:
				Intent intent = new Intent(this, ViewBidsActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_mymessages:
				/*Intent intent2 = new Intent(this, ViewMessagesActivity.class);
				startActivity(intent2);*/
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


}