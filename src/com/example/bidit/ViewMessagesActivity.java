package com.example.bidit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;

public class ViewMessagesActivity extends BiditActivity {
	
	private MessageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_messages);
		
		adapter = new MessageAdapter(this, R.layout.messages_list_item);
		ListView lv = (ListView)findViewById(R.id.message_list);
		lv.setAdapter(adapter);
		
		new RequestMessagesTask().execute();
	}
	
	public class MessageAdapter extends ArrayAdapter<Message> {

		public MessageAdapter(Context context, int resource) {
			super(context, resource);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.messages_list_item, null);
			}

			final Message it = this.getItem(position);
			TextView messageContent = (TextView) v.findViewById(R.id.msg_content);
			messageContent.setText(it.getSubject() + " - " + it.getContent());

			Button replyToBid = (Button) v.findViewById(R.id.reply_to_message);
			replyToBid.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					String subject = "RE:"+it.getSubject();
					if(subject.length() > 20)
					{
						subject = subject.substring(0,20);
					}
					Message msg = new Message(it.getReceiver(), it.getSender(), null, subject);
					SendMessageDialogFragment smdf = SendMessageDialogFragment.newInstance(msg);
					smdf.show(getFragmentManager(), "login");
				}
				
			});
			
			return v;
		}
	}
	
	public class RequestMessagesTask extends AsyncTask<Void, Message, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			String rangeurl = "";
			try {
				SharedPreferences pref = Util.getPreferences(getApplicationContext());
				rangeurl = "?q=" + URLEncoder.encode("{\"filters\":[{\"name\":\"receiver\",\"op\":\"eq\",\"val\":"+"\"", "UTF-8") 
						+ URLEncoder.encode(pref.getString("Username", "") + "\"}],", "UTF-8")
						+ URLEncoder.encode("\"order_by\":[{\"field\":\"id\",\"direction\":\"desc\"}]}", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewMessagesActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewMessagesActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e1),
							false)
						.build()
				);
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpGet request = new HttpGet(Util.MESSAGE_API + rangeurl);
			try {
				HttpResponse response = Util.getHttpClient()
						.execute(request);
				String content = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(content);
				JSONArray objects = json.getJSONArray("objects");
				for (int i = 0; i < objects.length(); ++i) {
					JSONObject o = objects.getJSONObject(i);
					User sender = new User(o.getString("sender"));
					User receiver = new User(o.getString("receiver"));;
					String msg_content = o.getString("content");
					String msg_subject = o.getString("subject");
					
					Message message = new Message(sender, receiver, msg_content, msg_subject);
					publishProgress(message);
				}
				Log.d(ViewMessagesActivity.class.getName(), content);
			} catch (ClientProtocolException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewMessagesActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewMessagesActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (IOException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewMessagesActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewMessagesActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			} catch (JSONException e) {
				EasyTracker easyTracker = EasyTracker.getInstance(ViewMessagesActivity.this);
				easyTracker.send(MapBuilder
						.createException(new StandardExceptionParser(ViewMessagesActivity.this, null)
							.getDescription(Thread.currentThread().getName(), e),
							false)
						.build()
				);
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Message... messages) {
			adapter.addAll(messages);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub

	}

}
