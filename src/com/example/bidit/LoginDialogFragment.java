package com.example.bidit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnLayoutChangeListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LoginDialogFragment extends DialogFragment {
	public LoginDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_login, null);

		builder.setView(view)
				.setPositiveButton(R.string.login,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface di, int id) {
								AlertDialog dialog = (AlertDialog)di;
								String text = dialog.getButton(DialogInterface.BUTTON_POSITIVE).getText().toString();
								if (text.equals("Login")) {
									new LoginTask(
											(OnLoginSuccessful) getActivity(),
											getActivity()).execute(view);
								} else {
									new RegisterTask();
								}
								
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								LoginDialogFragment.this.getDialog().cancel();
							}
						});
		
		final AlertDialog dialog = builder.create();
		final Switch s = ((Switch) view.findViewById(R.id.switchRegister));
		s.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
				boolean on = s.isChecked();
				if (on) {
					button.setText("Register");
				} else {
					button.setText("Login");
				}
			}
		});
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public class RegisterTask extends AsyncTask<View, Void, Boolean> {

		@Override
		protected Boolean doInBackground(View... arg0) {
			return null;
		}
	}


	public class LoginTask extends AsyncTask<View, Void, Boolean> {

		private ProgressDialog loggingInDialog;
		private OnLoginSuccessful myListener;
		private Context myContext;

		public LoginTask(OnLoginSuccessful listener, Context context) {
			this.myListener = listener;
			this.myContext = context;
		}

		public OnLoginSuccessful getMyListener() {
			return myListener;
		}

		public void setMyListener(OnLoginSuccessful myListener) {
			this.myListener = myListener;
		}

		public Context getMyContext() {
			return myContext;
		}

		public void setMyContext(Context myContext) {
			this.myContext = myContext;
		}

		@Override
		protected void onPreExecute() {
			loggingInDialog = new ProgressDialog(getActivity());
			loggingInDialog.setMessage("Logging In...");
			loggingInDialog.setCancelable(false);
			loggingInDialog.show();
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(loggingInDialog.getWindow().getAttributes());
			lp.width = 700;
			loggingInDialog.getWindow().setAttributes(lp);
		}

		@Override
		protected void onPostExecute(Boolean login) {
			if (login.booleanValue()) {
				loggingInDialog.dismiss();
				myListener.onLoginSuccessful();
				Toast.makeText(myContext, "Login Successful",
						Toast.LENGTH_SHORT).show();
			}

			else {
				Toast.makeText(myContext, "Incorrect Credentials",
						Toast.LENGTH_SHORT).show();
				loggingInDialog.dismiss();
			}

		}

		@Override
		protected Boolean doInBackground(View... views) {
			View view = views[0];
			String username = ((EditText) view.findViewById(R.id.username))
					.getText().toString();
			String password = ((EditText) view.findViewById(R.id.password))
					.getText().toString();
			try {
				HttpPost request = new HttpPost(Util.LOGIN);
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("email", username));
				pairs.add(new BasicNameValuePair("password", password));
				request.setEntity(new UrlEncodedFormEntity(pairs));

				HttpResponse response = Util.getHttpClient().execute(request);
				if (response.getStatusLine().getStatusCode() == 403) {
					return false;
				}

				SharedPreferences prefs = Util.getPreferences(getMyContext());
				Editor editor = prefs.edit();

				editor.putBoolean("isLoggedIn", true);
				editor.putString("Username", username);
				editor.putString("Password", password);
				editor.commit();

				Util.setCurrentUser(new User(username));

				String content = EntityUtils.toString(response.getEntity());
				Log.d("LoginDialog", content);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
}
