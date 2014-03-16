package com.example.bidit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginDialogFragment extends DialogFragment {
	public class LoginTask extends AsyncTask<View, Void, Boolean> {

		private ProgressDialog loggingInDialog;
		private OnLoginSuccessful myListener;
		private Context myContext;

		public LoginTask(OnLoginSuccessful listener, Context context) {
			this.myListener = listener;
			this.myContext = context;
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

		public Context getMyContext() {
			return myContext;
		}

		public OnLoginSuccessful getMyListener() {
			return myListener;
		}

		@Override
		protected void onPostExecute(Boolean login) {
			if (login.booleanValue()) {
				loggingInDialog.dismiss();
				if(postflag)
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

		public void setMyContext(Context myContext) {
			this.myContext = myContext;
		}

		public void setMyListener(OnLoginSuccessful myListener) {
			this.myListener = myListener;
		}
	}

	public class RegisterTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog progressDialog;
		private Context context;
		private String username;
		private String password;

		public RegisterTask(Context context, String username, String password) {
			super();
			this.context = context;
			this.username = username;
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				SharedPreferences prefs = Util.getPreferences(getActivity());
				HttpPost request = new HttpPost(Util.USER_API);
				JSONObject user = new JSONObject();
				user.put("email", username);
				user.put("password", password);
				Log.d("LoginDialogFragment", user.toString());		
				StringEntity entity = new StringEntity(user.toString());
				request.setEntity(entity);
				request.setHeader("Content-type", "application/json");
				HttpResponse response = Util.getHttpClient().execute(request);
				String content = EntityUtils.toString(response.getEntity());
				Log.d("LoginDialogFragment", content);
				if (response.getStatusLine().getStatusCode() == 201) {
					Editor editor = prefs.edit();
					editor.putBoolean("isLoggedIn", true);
					editor.putString("Username", username);
					editor.putString("Password", password);
					editor.commit();
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean successful) {
			if (successful) {
				Toast.makeText(context, "Registration succesful", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Could not register", Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Registering");
			progressDialog.show();
		}
		
	}

	private boolean postflag = false;
	
	public LoginDialogFragment() {

	}
	
	public LoginDialogFragment(boolean postflag)
	{
		this.postflag = postflag;
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
									String username = ((EditText) view.findViewById(R.id.username))
											.getText().toString();
									String password = ((EditText) view.findViewById(R.id.password))
											.getText().toString();
									new RegisterTask(getActivity(), username, password).execute();
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
					view.findViewById(R.id.confirm).setVisibility(View.VISIBLE);
				} else {
					button.setText("Login");
					view.findViewById(R.id.confirm).setVisibility(View.GONE);
				}
			}
		});
		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
				boolean on = s.isChecked();
				if (on) {
					button.setText("Register");
					view.findViewById(R.id.confirm).setVisibility(View.VISIBLE);
				} else {
					button.setText("Login");
					view.findViewById(R.id.confirm).setVisibility(View.GONE);
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
}
