package com.example.bidit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageDialogFragment extends DialogFragment {
	
	private Message msg;
	private View view;
	
	public static SendMessageDialogFragment newInstance(Message msg){
		SendMessageDialogFragment instance = new SendMessageDialogFragment(msg);
		return instance;
	}
	
	private SendMessageDialogFragment(Message msg)
	{
		this.msg = msg;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_sendmessage, null);

		builder.setView(view)
				.setPositiveButton(R.string.send,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface di, int id) {
								
								new SendMessageTask(msg, getActivity()).execute(view);
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
							}
						});
		
		return builder.create();
	}
	
	public class SendMessageTask extends AsyncTask<View, Void, Void> {
		private Message msg;
		Context context;
		
		public SendMessageTask(Message msg, Context context) {
			super();
			this.msg = msg;
			this.context = context;
		}

		@Override
		protected Void doInBackground(View... views) {
			View view = views[0];
			String content = ((EditText) view.findViewById(R.id.username))
					.getText().toString();
			try {
				//SharedPreferences prefs = Util.getPreferences(getActivity());
				HttpPost request = new HttpPost(Util.MESSAGE_API);
				JSONObject msgs = new JSONObject();
				msgs.put("sender", msg.getSender().getEmail());
				msgs.put("receiver", msg.getReceiver().getEmail());
				msgs.put("content", content);
				msgs.put("subject", msg.getSubject());
	
				StringEntity entity = new StringEntity(msgs.toString());
				request.setEntity(entity);
				request.setHeader("Content-type", "application/json");
				HttpResponse response = Util.getHttpClient().execute(request);
				//String content = EntityUtils.toString(response.getEntity());
				//Log.d("BidDialog", content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
		}
		
    }

}
