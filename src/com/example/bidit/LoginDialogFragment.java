package com.example.bidit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

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
							public void onClick(DialogInterface dialog, int id) {
								new LoginTask((OnLoginSuccessful)getActivity(), getActivity()).execute(view);
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								LoginDialogFragment.this.getDialog().cancel();
							}
						});
		return builder.create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
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
			// uploadingDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			loggingInDialog.setCancelable(false);
			loggingInDialog.show();

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

			lp.copyFrom(loggingInDialog.getWindow().getAttributes());
			lp.width = 700;
			// lp.height = 150;
			// lp.x=-170;
			// lp.y=100;
			loggingInDialog.getWindow().setAttributes(lp);
		}

		@Override
		protected void onPostExecute(Boolean login) {
			if (login.booleanValue()) {
				loggingInDialog.dismiss();
				myListener.onLoginSuccessful();
			} 
			
			else {
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
